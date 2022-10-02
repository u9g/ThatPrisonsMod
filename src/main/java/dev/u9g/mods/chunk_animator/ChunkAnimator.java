package dev.u9g.mods.chunk_animator;

public class ChunkAnimator {
    public static int mode = 0; // "How should the chunks be animated?\n 0: Chunks always appear from below\n 1: Chunks always appear from above\n 2: Chunks appear from below if they are lower than the Horizon and from above if they are higher than the Horizon\n 3: Chunks \"slide in\" from their respective cardinal direction (Relative to the Player)\n 4: Same as 3 but the cardinal direction of a chunk is determined slightly different (Just try both :D)"
    public static int animationDuration = 1000; // How long should the animation last? (In milliseconds)
    public static boolean disableAroundPlayer = false; // "If enabled chunks that are next to the player will not animate"
}
