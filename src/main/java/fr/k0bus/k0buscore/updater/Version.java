package fr.k0bus.k0buscore.updater;

import fr.k0bus.k0buscore.utils.MathUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a version of a plugin, including the numeric version and its type (e.g., RELEASE, BETA).
 * This class allows comparison between different versions to determine which one is newer.
 */
public class Version implements Comparable<Version>, Serializable {

    private List<Integer> num = new ArrayList<>();
    private VersionType type;
    private String version;

    /**
     * Constructs a new {@link Version} object from a version string.
     * The version string is parsed, and the numeric version is extracted.
     * The version type (e.g., RELEASE, BETA) is also determined based on the version string.
     *
     * @param version The version string to parse and represent.
     */
    public Version(String version)
    {
        this.version = version;
        // Determine the version type (e.g., RELEASE, BETA, etc.)
        for (VersionType vType: VersionType.values()) {
            String v = version.toLowerCase();
            if(v.contains(vType.getName())) {
                this.type = vType;
                break;
            }
        }
        // Default type if not found
        if(type == null)
            type = VersionType.RELEASE;

        // Clean the version string to only contain numeric values and periods
        version = version.replaceAll("[^\\d.]", "");
        String[] args = version.split("\\.");
        // Parse each part of the version string into an integer list
        for (String v : args) {
            if(MathUtils.isInteger(v)) {
                num.add(Integer.parseInt(v));
            }
        }
    }

    /**
     * Returns a string representation of the version.
     *
     * @return The version string.
     */
    @Override
    public String toString()
    {
        return version;
    }

    /**
     * Compares this {@link Version} object to another {@link Version} object.
     * The comparison is made first based on the numeric components of the version and then by the version type.
     * The order is determined by comparing each numeric part of the version in order.
     *
     * @param toCompare The {@link Version} object to compare with.
     * @return A negative integer, zero, or a positive integer as this version is less than, equal to, or greater than the specified version.
     */
    @Override
    public int compareTo(Version toCompare) {
        if(toCompare == null)
            return 1;

        int i = 0;
        for (int n : num) {
            int compare = 0;
            if(toCompare.num.size() > i)
                compare = toCompare.num.get(i);
            if(n > compare)
                return 1;
            if(n < compare)
                return -1;
            i++;
        }

        // Compare version types (e.g., BETA < RELEASE)
        if(type.level < toCompare.type.level)
            return -1;
        else if(type.level > toCompare.type.level)
            return 1;

        return 0;
    }

    /**
     * Gets the version string of this version.
     *
     * @return The version string.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the numeric components of this version.
     *
     * @return The list of integers representing the numeric components of the version.
     */
    public List<Integer> getNum() {
        return num;
    }

    /**
     * Gets the type of this version (e.g., RELEASE, BETA).
     *
     * @return The version type.
     */
    public VersionType getType() {
        return type;
    }
}
