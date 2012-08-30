import java.util.Arrays;


public class OTileEntityFurnace extends OTileEntity implements OIInventory, Container<OItemStack> {

    private OItemStack[] d = new OItemStack[3];
    public int a = 0;
    public int b = 0;
    public int c = 0;
    private String name = "container.furnace"; // CanaryMod

    public OTileEntityFurnace() {
        super();
    }

    public int i_() {
        return this.d.length;
    }

    public OItemStack a(int i) {
        return this.d[i];
    }

    public OItemStack a(int i, int j) {
        if (this.d[i] != null) {
            OItemStack oitemstack;

            if (this.d[i].a <= j) {
                oitemstack = this.d[i];
                this.d[i] = null;
                return oitemstack;
            } else {
                oitemstack = this.d[i].a(j);
                if (this.d[i].a == 0) {
                    this.d[i] = null;
                }

                return oitemstack;
            }
        } else {
            return null;
        }
    }

    public OItemStack b(int i) {
        if (this.d[i] != null) {
            OItemStack oitemstack = this.d[i];

            this.d[i] = null;
            return oitemstack;
        } else {
            return null;
        }
    }

    public void a(int i, OItemStack oitemstack) {
        this.d[i] = oitemstack;
        if (oitemstack != null && oitemstack.a > this.j_()) {
            oitemstack.a = this.j_();
        }

    }

    public String b() {
        return name;
    }

    public void a(ONBTTagCompound onbttagcompound) {
        super.a(onbttagcompound);
        ONBTTagList onbttaglist = onbttagcompound.m("Items");

        this.d = new OItemStack[this.i_()];

        for (int i = 0; i < onbttaglist.c(); ++i) {
            ONBTTagCompound onbttagcompound1 = (ONBTTagCompound) onbttaglist.b(i);
            byte b0 = onbttagcompound1.c("Slot");

            if (b0 >= 0 && b0 < this.d.length) {
                this.d[b0] = OItemStack.a(onbttagcompound1);
            }
        }

        this.a = onbttagcompound.d("BurnTime");
        this.c = onbttagcompound.d("CookTime");
        this.b = a(this.d[1]);
    }

    public void b(ONBTTagCompound onbttagcompound) {
        super.b(onbttagcompound);
        onbttagcompound.a("BurnTime", (short) this.a);
        onbttagcompound.a("CookTime", (short) this.c);
        ONBTTagList onbttaglist = new ONBTTagList();

        for (int i = 0; i < this.d.length; ++i) {
            if (this.d[i] != null) {
                ONBTTagCompound onbttagcompound1 = new ONBTTagCompound();

                onbttagcompound1.a("Slot", (byte) i);
                this.d[i].b(onbttagcompound1);
                onbttaglist.a((ONBTBase) onbttagcompound1);
            }
        }

        onbttagcompound.a("Items", (ONBTBase) onbttaglist);
    }

    public int j_() {
        return 64;
    }

    public boolean i() {
        return this.a > 0;
    }

    public void g() {
        boolean flag = this.a > 0;
        boolean flag1 = false;

        if (this.a > 0) {
            --this.a;
        }

        if (!this.k.K) {
            if (this.a == 0 && this.r()) {
                this.b = this.a = a(this.d[1]);
                if (this.a > 0) {
                    flag1 = true;
                    if (this.d[1] != null) {
                        --this.d[1].a;
                        if (this.d[1].a == 0) {
                            OItem oitem = this.d[1].b().q();

                            this.d[1] = oitem != null ? new OItemStack(oitem) : null;
                        }
                    }
                }
            }

            if (this.i() && this.r()) {
                ++this.c;
                if (this.c == 200) {
                    this.c = 0;
                    this.k();
                    flag1 = true;
                }
            } else {
                this.c = 0;
            }

            if (flag != this.a > 0) {
                flag1 = true;
                OBlockFurnace.a(this.a > 0, this.k, this.l, this.m, this.n);
            }
        }

        if (flag1) {
            this.d();
        }

    }

    private boolean r() {
        if (this.d[0] == null) {
            return false;
        } else {
            OItemStack oitemstack = OFurnaceRecipes.a().b(this.d[0].b().bT);

            return oitemstack == null ? false : (this.d[2] == null ? true : (!this.d[2].a(oitemstack) ? false : (this.d[2].a < this.j_() && this.d[2].a < this.d[2].d() ? true : this.d[2].a < oitemstack.d())));
        }
    }

    public void k() {
        if (this.r()) {
            OItemStack oitemstack = OFurnaceRecipes.a().b(this.d[0].b().bT);

            if (this.d[2] == null) {
                this.d[2] = oitemstack.l();
            } else if (this.d[2].c == oitemstack.c) {
                ++this.d[2].a;
            }

            --this.d[0].a;
            if (this.d[0].a <= 0) {
                this.d[0] = null;
            }

        }
    }

    public static int a(OItemStack oitemstack) {
        if (oitemstack == null) {
            return 0;
        } else {
            int i = oitemstack.b().bT;
            OItem oitem = oitemstack.b();

            if (i < 256 && OBlock.m[i] != null) {
                OBlock oblock = OBlock.m[i];

                if (oblock == OBlock.bO) {
                    return 150;
                }

                if (oblock.cp == OMaterial.d) {
                    return 300;
                }
            }

            return oitem instanceof OItemTool && ((OItemTool) oitem).e().equals("WOOD") ? 200 : (oitem instanceof OItemSword && ((OItemSword) oitem).f().equals("WOOD") ? 200 : (oitem instanceof OItemHoe && ((OItemHoe) oitem).f().equals("WOOD") ? 200 : (i == OItem.D.bT ? 100 : (i == OItem.m.bT ? 1600 : (i == OItem.ay.bT ? 20000 : (i == OBlock.y.ca ? 100 : (i == OItem.bo.bT ? 2400 : 0)))))));
        }
    }

    public static boolean b(OItemStack oitemstack) {
        return a(oitemstack) > 0;
    }

    public boolean a(OEntityPlayer oentityplayer) {
        return this.k.p(this.l, this.m, this.n) != this ? false : oentityplayer.e((double) this.l + 0.5D, (double) this.m + 0.5D, (double) this.n + 0.5D) <= 64.0D;
    }

    public void k_() {}

    public void f() {}

    @Override
    public OItemStack[] getContents() {
        return Arrays.copyOf(this.d, this.getContentsSize());
    }

    @Override
    public void setContents(OItemStack[] aoitemstack) {
        this.d = Arrays.copyOf(aoitemstack, this.getContentsSize());
    }

    @Override
    public OItemStack getContentsAt(int i) {
        return this.a(i);
    }

    @Override
    public void setContentsAt(int i, OItemStack oitemstack) {
        this.a(i, oitemstack);
    }

    @Override
    public int getContentsSize() {
        return this.i_();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String s) {
        this.name = s;
    }

}
