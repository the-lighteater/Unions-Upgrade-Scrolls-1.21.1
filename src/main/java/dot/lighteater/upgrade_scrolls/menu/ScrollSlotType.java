package dot.lighteater.upgrade_scrolls.menu;

import java.util.Set;

public enum ScrollSlotType {

    CURSED(
            Set.of(
                    1,
                    2,
                    9,
                    10,
                    11,
                    12,
                    13,
                    23
            )
    ),

    AFFIX(
            Set.of(
                    3,
                    4,
                    5,
                    6,
                    7,
                    8,
                    22
            )
    ),

    HOLY(
            Set.of(
                    21
            )
    ),

    GOLDEN(
            Set.of(
                    14,
                    15,
                    16,
                    17,
                    18,
                    19,
                    24
            )
    );

    private final Set<Integer> validScrollIds;

    ScrollSlotType(Set<Integer> validScrollIds) {
        this.validScrollIds = validScrollIds;
    }

    public boolean accepts(int scrollId) {
        return validScrollIds.contains(scrollId);
    }

    public Set<Integer> getValidScrollIds() {
        return validScrollIds;
    }
}