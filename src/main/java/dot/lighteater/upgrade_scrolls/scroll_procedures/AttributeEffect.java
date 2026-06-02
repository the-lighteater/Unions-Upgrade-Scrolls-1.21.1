package dot.lighteater.upgrade_scrolls.scroll_procedures;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AttributeEffect {

    private static final Logger LOGGER = LogManager.getLogger();

    private String attribute;
    private String operation;
    private double value;
    private String endString;

    public Holder.Reference<Attribute> getAttribute() {
        ResourceLocation id = ResourceLocation.parse(attribute);


        return BuiltInRegistries.ATTRIBUTE.getHolder(id).orElse(null);
    }

    public AttributeModifier.Operation getOperation() {
        return switch (operation) {
            case "Adding" -> AttributeModifier.Operation.ADD_VALUE;
            default -> AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
        };
    }

    public double getValue() {
        return value;
    }

    public String getEndString() {
        return endString;
    }
}
