package vn.uit.clothesshop.dto.response;

public final class CategoryBasicInfoResponseDto {
    private final long id;
    private final String name;
    private final String desc;

    public CategoryBasicInfoResponseDto(
            final long id,
            final String name,
            final String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

}
