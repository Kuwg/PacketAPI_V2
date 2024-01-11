package kuwg.packetapi.mojang;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

public class PropertyMap extends ForwardingMultimap<String, Property> {
    private final Multimap<String, Property> properties = LinkedHashMultimap.create();

    public PropertyMap() {
    }

    protected @NotNull Multimap<String, Property> delegate() {
        return this.properties;
    }

    @SuppressWarnings("rawtypes")
    public static class Serializer implements JsonSerializer<PropertyMap>, JsonDeserializer<PropertyMap> {
        public Serializer() {
        }

        public PropertyMap deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
            PropertyMap var4 = new PropertyMap();
            if (var1 instanceof JsonObject) {
                JsonObject var5 = (JsonObject)var1;
                Iterator<Map.Entry<String, JsonElement>> var6 = var5.entrySet().iterator();

                while(true) {
                    Map.Entry var7;
                    do {
                        if (!var6.hasNext()) {
                            return var4;
                        }

                        var7 = var6.next();
                    } while(!(var7.getValue() instanceof JsonArray));

                    for (JsonElement var9 : (JsonArray) var7.getValue()) {
                        var4.put((String) var7.getKey(), new Property((String) var7.getKey(), var9.getAsString()));
                    }
                }
            } else if (var1 instanceof JsonArray) {
                for (JsonElement var11 : (JsonArray) var1) {
                    if (var11 instanceof JsonObject) {
                        JsonObject var12 = (JsonObject) var11;
                        String var13 = var12.getAsJsonPrimitive("name").getAsString();
                        String var14 = var12.getAsJsonPrimitive("value").getAsString();
                        if (var12.has("signature")) {
                            var4.put(var13, new Property(var13, var14, var12.getAsJsonPrimitive("signature").getAsString()));
                        } else {
                            var4.put(var13, new Property(var13, var14));
                        }
                    }
                }
            }

            return var4;
        }

        @Override
        public JsonElement serialize(PropertyMap var1, Type var2, JsonSerializationContext var3) {
            JsonArray var4 = new JsonArray();
            JsonObject var7;
            for (Iterator<Property> var5 = var1.values().iterator(); var5.hasNext(); var4.add(var7)) {
                Property var6 = var5.next();
                var7 = new JsonObject();
                var7.addProperty("name", var6.getName());
                var7.addProperty("value", var6.getValue());
                if (var6.hasSignature()) {
                    var7.addProperty("signature", var6.getSignature());
                }
            }

            return var4;
        }
    }
}
