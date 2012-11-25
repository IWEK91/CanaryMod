/**
 * ComplexBlock.java - Interface for complex blocks like chests and signs
 * 
 * @author James
 */
public interface ComplexBlock extends Metadatable {

    /**
     * Returns the X coordinates of this block
     * 
     * @return X
     */
    public int getX();

    /**
     * Returns the Y coordinates of this block
     * 
     * @return Y
     */
    public int getY();

    /**
     * Returns the Z coordinates of this block
     * 
     * @return Z
     */
    public int getZ();

    /**
     * Sends the updated block information to clients.
     */
    public void update();

    /**
     * Returns the Block associated with this ComplexBlock
     * 
     * @return Block
     */
    public Block getBlock();

    /**
     * Returns the World associated with this ComplexBlock
     * @return World
     */
    public World getWorld();
    
}
