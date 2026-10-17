package dot.lighteater.upgrade_scrolls.affix;

import net.minecraft.ChatFormatting;

import java.util.List;
import java.util.Map;

public class AffixData {

    private String name;
    private double weight;
    private String colorNum;
    private String displayName;
    private boolean disabled;
    private boolean finalAffix;
    private Map<String, List<AttributeEffect>> effects;
    private List<String> sounds;
    private boolean hidden;
    private double percentage;

    public String getName() {
        return name != null ? name : "";
    }
    public double getWeight() {
        return weight;
    }

    public int getColorNumber() {
        if (colorNum == null)
            return 0xFFFFFFFF;

        int color;

        try {
            color = (int) Long.parseLong(
                    colorNum
                            .replace("0x", "")
                            .replace("#", ""),
                    16
            );
        } catch (NumberFormatException e) {
            return 0xFFFFFFFF;
        }

        return color;
    }

    public List<AttributeEffect> getEffectsFor(String type) {
        if (effects == null) {
            return List.of();
        }
        return effects.getOrDefault(type, List.of());
    }

    public String getDisplayName() {
        return displayName != null ? displayName : "";
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isFinal() {
        return finalAffix;
    }

    public List<String> getSounds() {
        return sounds != null ? sounds : List.of();
    }

    public boolean isHidden() {return hidden;}

    public void setPercentage(double value) {percentage = value;}

    public double getPercentage() {return percentage;}
}