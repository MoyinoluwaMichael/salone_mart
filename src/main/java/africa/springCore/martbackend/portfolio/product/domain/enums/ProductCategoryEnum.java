package africa.springCore.martbackend.portfolio.product.domain.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum ProductCategoryEnum {
    FASHION("Fashion", List.of("Adidas", "Nike", "Puma", "Reebok", "Asics", "Vans", "Fila", "Fila", "Fila", "Fila"), List.of("T-Shirts", "Polos", "Hoodies", "Sweatshirts", "Jeans", "Pants", "Shorts", "Dresses", "Shoes", "Accessories", "Jewelry", "Watches", "Sunglasses", "Bags")),
    ELECTRONICS("Electronics", List.of("Apple", "Samsung", "Sony", "LG", "Bose", "JBL", "Beats", "Sonos", "Panasonic", "Philips"), List.of("TVs", "Home Theaters", "Headphones", "Earbuds", "Speakers", "Soundbars", "Cameras", "Drones", "Smartwatches", "Fitness Trackers", "Gaming Consoles", "Video Games", "Laptops", "Desktops", "Monitors", "Printers", "Scanners", "Projectors", "Networking", "Storage", "Software", "Accessories")),
    HOME_AND_OFFICE("Home & Office", List.of("Samsung", "LG", "Apple", "Sony", "Bose", "JBL", "Beats", "Sonos", "Panasonic", "Philips"), List.of("Bedroom Furniture", "Bathroom Furniture", "Kitchen Appliances", "Tableware", "Home Decor", "Office Furniture", "Lighting", "Furniture Accessories", "Wall Art", "Window Treatments", "Flooring", "Doors", "Ceilings", "Storage", "Accessories")),
    PHONES_AND_TABLETS("Phones & Tablets", List.of("Apple", "Samsung", "Itel", "Tecno"), List.of("Smartphones", "Android Phones", "iPhones", "Basic Phones", "Refurbished Phones", "iPads", "Android Tablets", "Tablet Accessories", "Power Banks", "Screen Protectors")),
    COMPUTING("Computing", List.of("Apple", "Samsung", "Sony", "LG", "Bose", "JBL", "Beats", "Sonos", "Panasonic", "Philips"), List.of("Laptops", "Desktops", "Monitors", "Printers", "Scanners", "Projectors", "Networking", "Storage", "Software", "Accessories")),
    SUPERMARKET("Supermarket", List.of("Apple", "Samsung", "Sony", "LG", "Bose", "JBL", "Beats", "Sonos", "Panasonic", "Philips"), List.of("Groceries", "Beverages", "Snacks", "Household Supplies", "Personal Care", "Baby Care", "Pet Supplies", "Health & Wellness", "Beauty & Cosmetics", "Stationery", "Books", "Toys", "Games", "Outdoor & Garden", "Automotive", "Tools & Home Improvement", "Electrical", "Plumbing", "Hardware", "Painting", "Building Materials", "Safety & Security", "Kitchen & Dining", "Bed & Bath", "Furniture", "Home Appliances", "Home Decor", "Lighting", "Storage", "Cleaning Supplies", "Laundry", "Gardening", "Outdoor Living", "Patio", "Grilling", "Outdoor Cooking", "Outdoor Heating", "Outdoor Lighting", "Outdoor Decor", "Outdoor Power Equipment", "Outdoor Storage", "Outdoor Play", "Outdoor Sports", "Outdoor Recreation", "Outdoor Apparel", "Outdoor Footwear", "Outdoor Accessories", "Outdoor Gear", "Outdoor Technology", "Outdoor Brands", "Outdoor Deals", "Outdoor Sale", "Outdoor Clearance", "Outdoor New Arrivals", "Outdoor Best Sellers", "Outdoor Top Rated", "Outdoor Customer Favorites", "Outdoor Gift Guide", "Outdoor Gift Cards", "Outdoor Gift Ideas", "Outdoor Gift Registry", "Outdoor Gift Shop", "Outdoor Gift Sets", "Outdoor Gift Baskets", "Outdoor Gift Boxes", "Outdoor Gift Bags", "Outdoor Gift Wrapping", "Outdoor Gift Tags", "Outdoor Gift Receipts", "Outdoor Gift Returns", "Outdoor Gift Exchanges", "Outdoor Gift Refunds", "Outdoor Gift Vouchers", "Outdoor Gift Certificates", "Outdoor Gift Coupons", "Outdoor Gift Discounts", "Outdoor Gift Sales", "Outdoor Gift Promotions", "Outdoor Gift Events", "Outdoor Gift Specials", "Outdoor Gift Offers", "Outdoor Gift Deals", "Outdoor Gift Bundles", "Outdoor Gift Packages", "Outdoor Gift Kits", "Outdoor Gift Collections", "Outdoor Gift Series", "Outdoor Gift Sequels", "Outdoor Gift Prequels", "Outdoor Gift Spinoffs", "Outdoor Gift Reboots", "Outdoor Gift Remakes", "Outdoor Gift Adaptations", "Outdoor Gift Reimaginings", "Outdoor Gift Reinterpretations", "Outdoor Gift Revisions", "Outdoor Gift"));

    private final String description;
    private final List<String> brands;
    private final List<String> productTypes;

    ProductCategoryEnum(String description, List<String> brands, List<String> productTypes) {
        this.description = description;
        this.brands = brands;
        this.productTypes = productTypes;
    }

    public static ProductCategoryEnum instanceOf(String name) {
        for (ProductCategoryEnum category : values()) {
            if (category.name().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }
}
