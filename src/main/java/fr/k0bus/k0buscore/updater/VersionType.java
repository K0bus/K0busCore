package fr.k0bus.k0buscore.updater;

/**
 * Enum representing the different types of versions for a plugin.
 * Each version type is associated with a name, a level, and a stability status.
 */
public enum VersionType {

    /**
     * Represents the pre-alpha version, typically used for very early development.
     */
    PREALPHA("pre-alpha", 0, false),

    /**
     * Represents the alpha version, often unstable and used for initial testing.
     */
    ALPHA("alpha", 1, false),

    /**
     * Represents the beta version, which is feature-complete but might still have bugs.
     */
    BETA("beta", 2, false),

    /**
     * Represents the release candidate version, which is close to stable but may still have minor issues.
     */
    RC("rc", 3, false),

    /**
     * Represents the stable version, which is considered ready for production use.
     */
    STABLE("stable", 4, true),

    /**
     * Represents the official release version, which is the final version.
     */
    RELEASE("release", 5, true),

    /**
     * Represents the snapshot version, which is a developmental version often subject to frequent changes.
     */
    SNAPSHOT("snapshot", 6, false);

    final String name;
    final int level;
    final boolean stable;

    /**
     * Constructs a VersionType with the specified name, level, and stability status.
     *
     * @param name The name of the version type (e.g., "alpha", "beta").
     * @param level The level of the version, indicating its relative stage in the development process.
     * @param stable A boolean indicating whether the version is stable and ready for production use.
     */
    VersionType(String name, int level, boolean stable) {
        this.name = name;
        this.level = level;
        this.stable = stable;
    }

    /**
     * Gets the name of the version type.
     *
     * @return The name of the version type.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the level of the version type.
     * The level indicates the relative stage of the version.
     *
     * @return The level of the version type.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Checks if the version type is stable.
     *
     * @return True if the version type is stable, otherwise false.
     */
    public boolean isStable() {
        return stable;
    }
}
