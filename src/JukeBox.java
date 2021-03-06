/**
 * JukeBox/RecordPlayer interface
 *
 * @author Drathus42
 */
public class JukeBox implements ComplexBlock {
    OTileEntityRecordPlayer oRP;

    public JukeBox(OTileEntityRecordPlayer oRP) {

        this.oRP = oRP;
    }

    @Override
    public int getX() {

        return oRP.l;
    }

    @Override
    public int getY() {

        return oRP.m;
    }

    @Override
    public int getZ() {

        return oRP.n;
    }

    @Override
    public void update() {
        oRP.g();
    }

    @Override
    public Block getBlock() {

        return getWorld().getBlockAt(getX(), getY(), getZ());
    }

    @Override
    public World getWorld() {

        return oRP.k.world;
    }

    /**
     * Check if a record is present in the JukeBox
     * @return true if record present, false if no record present
     */
    public boolean hasRecord() {

        return (oRP.p == 1 ? true : false);
    }

    /**
     * Get the ID of the record in the JukeBox (if any)
     * @return Item ID number of record or -1 if no record present
     */
    public int getDiscID() {

        return (hasRecord() ? oRP.a.j() : -1);
    }

    /**
     * Get the item currently in the jukebox (if any)
     * @return The record <tt>Item</tt> or null if no record present
     */
    public Item getDisc() {
        return hasRecord() ? new Item(oRP.a) : null;
    }

    @Override
    public NBTTagCompound getMetaTag() {
        return oRP.metadata;
    }

    @Override
    public void writeToTag(NBTTagCompound tag) {
        oRP.b(tag.getBaseTag());
    }

    @Override
    public void readFromTag(NBTTagCompound tag) {
        oRP.a(tag.getBaseTag());
    }
}
