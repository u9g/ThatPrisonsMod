package dev.u9g.mods.chunk_animator;

import java.util.WeakHashMap;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.BuiltChunk;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class AnimationHandler
{
	private static WeakHashMap<BuiltChunk, AnimationData> timeStamps = new WeakHashMap<>();

	public static void preRender(BuiltChunk renderChunk)
	{
		if (timeStamps.containsKey(renderChunk))
		{
			AnimationData animationData = timeStamps.get(renderChunk);
			long time = animationData.timeStamp;
			int mode = ChunkAnimator.mode;

			if (time == -1L)
			{
				time = System.currentTimeMillis();

				animationData.timeStamp = time;

				// Mode 4 Set Chunk Facing
				if (mode == 4)
				{
					BlockPos zeroedPlayerPosition = forgePosition(MinecraftClient.getInstance().player);
					zeroedPlayerPosition = zeroedPlayerPosition.add(0, -zeroedPlayerPosition.getY(), 0);

					BlockPos zeroedCenteredChunkPos = renderChunk.getPos().add(8, -renderChunk.getPos().getY(), 8);

					Vec3i dif = zeroedPlayerPosition.subtract(zeroedCenteredChunkPos);

					int difX = Math.abs(dif.getX());
					int difZ = Math.abs(dif.getZ());

					Direction chunkFacing;

					if (difX > difZ)
					{
						if (dif.getX() > 0)
						{
							chunkFacing = Direction.EAST;
						}
						else
						{
							chunkFacing = Direction.WEST;
						}
					}
					else
					{
						if (dif.getZ() > 0)
						{
							chunkFacing = Direction.SOUTH;
						}
						else
						{
							chunkFacing = Direction.NORTH;
						}
					}

					animationData.chunkFacing = chunkFacing;
				}
			}

			long timeDif = System.currentTimeMillis() - time;

			int animationDuration = ChunkAnimator.animationDuration;

			if (timeDif < animationDuration)
			{
				double chunkY = renderChunk.getPos().getY();
				double modY;

				if (mode == 2)
				{
					if (chunkY < MinecraftClient.getInstance().world.dimension.method_3994()/*getHorizon*/)
					{
						mode = 0;
					}
					else
					{
						mode = 1;
					}
				}

				if (mode == 4)
				{
					mode = 3;
				}

				switch (mode)
				{
					case 0:
						modY = chunkY / (animationDuration) * timeDif;
						GlStateManager.translated(0, -chunkY + modY, 0);
						break;
					case 1:
						modY = (256D - chunkY) / (animationDuration) * timeDif;
						GlStateManager.translated(0, 256 - chunkY - modY, 0);
						break;
					case 3:
						Direction direction = animationData.chunkFacing;

						if (direction != null)
						{
							Vec3i vec = direction.getVector();
							double mod = -(200D - (200D / animationDuration * timeDif));

							GlStateManager.translated(vec.getX() * mod, 0, vec.getZ() * mod);
						}
						break;
				}
			}
			else
			{
				timeStamps.remove(renderChunk);
			}
		}
	}

	private static BlockPos forgePosition(ClientPlayerEntity player) {
		Vec3d temp = MinecraftClient.getInstance().player.getPos();
		return new BlockPos(temp.x+0.5, temp.y+0.5, temp.z + 0.5);
	}

	public static void setPosition(BuiltChunk renderChunk, BlockPos position)
	{
		if (MinecraftClient.getInstance().player != null)
		{
			boolean flag = true;
			BlockPos zeroedPlayerPosition = forgePosition(MinecraftClient.getInstance().player);
			zeroedPlayerPosition = zeroedPlayerPosition.add(0, -zeroedPlayerPosition.getY(), 0);
			BlockPos zeroedCenteredChunkPos = position.add(8, -position.getY(), 8);

			if (ChunkAnimator.disableAroundPlayer)
			{
				flag = zeroedPlayerPosition.getSquaredDistance(zeroedCenteredChunkPos) > (64 * 64);
			}

			if (flag)
			{
				Direction direction = null;

				if (ChunkAnimator.mode == 3)
				{
					Vec3i dif = zeroedPlayerPosition.subtract(zeroedCenteredChunkPos);

					int difX = Math.abs(dif.getX());
					int difZ = Math.abs(dif.getZ());

					if (difX > difZ)
					{
						if (dif.getX() > 0)
						{
							direction = Direction.EAST;
						}
						else
						{
							direction = Direction.WEST;
						}
					}
					else
					{
						if (dif.getZ() > 0)
						{
							direction = Direction.SOUTH;
						}
						else
						{
							direction = Direction.NORTH;
						}
					}
				}

				AnimationData animationData = new AnimationData(-1L, direction);
				timeStamps.put(renderChunk, animationData);
			}
		}
	}

	private static class AnimationData
	{
		public long timeStamp;

		public Direction chunkFacing;

		public AnimationData(long timeStamp, Direction chunkFacing)
		{
			this.timeStamp = timeStamp;
			this.chunkFacing = chunkFacing;
		}
	}
}
