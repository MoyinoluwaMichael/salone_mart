package africa.techimmortal.martbackend.portfolio.system.domain.model;

public enum MediaCategory {
    USER,
    DOCUMENT,
    PRODUCT;

    public static MediaCategory instanceOf(String type) {
        for (MediaCategory mediaCategory : MediaCategory.values()) {
            if (mediaCategory.name().equalsIgnoreCase(type)) {
                return mediaCategory;
            }
        }
        return null;
    }
}
