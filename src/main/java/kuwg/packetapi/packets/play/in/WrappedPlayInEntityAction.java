package kuwg.packetapi.packets.play.in;

import io.netty.buffer.ByteBuf;
import kuwg.packetapi.PacketAPI;
import kuwg.packetapi.events.PacketReceiveEvent;
import kuwg.packetapi.packets.EnumPacketDirection;
import kuwg.packetapi.packets.PacketWrapper;
import kuwg.packetapi.util.PAPIVer;
import org.bukkit.entity.Player;

public class WrappedPlayInEntityAction extends PacketWrapper<WrappedPlayInEntityAction> {
    public WrappedPlayInEntityAction(ByteBuf buffer, Player sender, EnumPacketDirection direction) {
        super(buffer, sender, direction);
    }
    private int entityID;
    private Action action;
    private int jumpBoost;

    public WrappedPlayInEntityAction(PacketReceiveEvent event) {
        super(event);
    }


    @Override
    public void read() {
        this.entityID = lastPast ? readVarInt() : readInt();
        int id = lastPast ? readVarInt() : readByte();
        this.action = Action.getById(id);
        this.jumpBoost = lastPast ? readVarInt() : readInt();
    }
    private static final boolean lastPast = PacketAPI.getVer().isOlderThan(new PAPIVer("1.8"));
    @Override
    public void write() {
        if (lastPast) {
            writeVarInt(this.entityID);
            int actionIndex = this.action.getId();
            writeVarInt(actionIndex);
            writeVarInt(this.jumpBoost);
        } else {
            writeInt(this.entityID);
            int actionIndex = this.action.getId();
            writeByte(actionIndex);
            writeInt(this.jumpBoost);
        }
    }


    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getJumpBoost() {
        return jumpBoost;
    }

    public void setJumpBoost(int jumpBoost) {
        this.jumpBoost = jumpBoost;
    }

    public enum Action {
        START_SNEAKING,
        STOP_SNEAKING,
        LEAVE_BED,
        START_SPRINTING,
        STOP_SPRINTING,
        START_JUMPING_WITH_HORSE,
        STOP_JUMPING_WITH_HORSE,
        OPEN_HORSE_INVENTORY,
        START_FLYING_WITH_ELYTRA;

        private static final Action[] VALUES = values();

        public int getId() {
            int actionIndex = ordinal();
            if (past) {
                if (this == OPEN_HORSE_INVENTORY) {
                    actionIndex--;
                }
            }
            return actionIndex;
        }

        public static Action getById(int id) {
            if (id >= VALUES.length || id < 0) {
                throw new IllegalStateException("EntityAction action out of bounds: " + id);
            }
            Action action = Action.VALUES[id];
            if (past) {
                if (action == Action.STOP_JUMPING_WITH_HORSE) {
                    action = Action.OPEN_HORSE_INVENTORY;
                }
            }
            return action;
        }
    }
    private static final boolean past = PacketAPI.getVer().isOlderThan(new PAPIVer("1.9"));
}
