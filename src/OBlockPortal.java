import java.util.Random;


public class OBlockPortal extends OBlockBreakable {

    public OBlockPortal(int i, int j) {
        super(i, j, OMaterial.B, false);
        this.b(true);
    }

    public void b(OWorld oworld, int i, int j, int k, Random random) {
        super.b(oworld, i, j, k, random);
        if (oworld.w.d() && random.nextInt(2000) < oworld.u) {
            int l;

            for (l = j; !oworld.t(i, l, k) && l > 0; --l) {
                ;
            }

            if (l > 0 && !oworld.s(i, l + 1, k)) {
                OItemMonsterPlacer.a(oworld, 57, (double) i + 0.5D, (double) l + 1.1D, (double) k + 0.5D);
            }
        }
    }

    public OAxisAlignedBB e(OWorld oworld, int i, int j, int k) {
        return null;
    }

    public void a(OIBlockAccess oiblockaccess, int i, int j, int k) {
        float f;
        float f1;

        if (oiblockaccess.a(i - 1, j, k) != this.ca && oiblockaccess.a(i + 1, j, k) != this.ca) {
            f = 0.125F;
            f1 = 0.5F;
            this.a(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
        } else {
            f = 0.5F;
            f1 = 0.125F;
            this.a(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
        }

    }

    public boolean d() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public boolean i_(OWorld oworld, int i, int j, int k) {
        byte b0 = 0;
        byte b1 = 0;

        if (oworld.a(i - 1, j, k) == OBlock.ap.ca || oworld.a(i + 1, j, k) == OBlock.ap.ca) {
            b0 = 1;
        }

        if (oworld.a(i, j, k - 1) == OBlock.ap.ca || oworld.a(i, j, k + 1) == OBlock.ap.ca) {
            b1 = 1;
        }

        if (b0 == b1) {
            return false;
        } else {
            if (oworld.a(i - b0, j, k - b1) == 0) {
                i -= b0;
                k -= b1;
            }

            int l;
            int i1;

            for (l = -1; l <= 2; ++l) {
                for (i1 = -1; i1 <= 3; ++i1) {
                    boolean flag = l == -1 || l == 2 || i1 == -1 || i1 == 3;

                    if (l != -1 && l != 2 || i1 != -1 && i1 != 3) {
                        int j1 = oworld.a(i + b0 * l, j + i1, k + b1 * l);

                        if (flag) {
                            if (j1 != OBlock.ap.ca) {
                                return false;
                            }
                        } else if (j1 != 0 && j1 != OBlock.ar.ca) {
                            return false;
                        }
                    }
                }
            }

            // CanaryMod hook onPortalCreate
            Block[][] portalBlocks = new Block[3][2];

            for (i1 = 0; i1 < 3; ++i1) {
                for (l = 0; l < 2; ++l) {
                    portalBlocks[i1][l] = new Block(oworld.world, Block.Type.Portal.getType(), i + b0 * l, j + 2 - i1, k + b1 * l);
                }
            }
            if (!(Boolean) etc.getLoader().callHook(PluginLoader.Hook.PORTAL_CREATE, (Object) portalBlocks)) { // Cast to make single argument
                oworld.t = true;

                for (l = 0; l < 2; ++l) {
                    for (i1 = 0; i1 < 3; ++i1) {
                        oworld.e(i + b0 * l, j + i1, k + b1 * l, OBlock.be.ca);
                    }
                }

                oworld.t = false;
                return true;
            }
            return false;
        }
    }

    public void a(OWorld oworld, int i, int j, int k, int l) {
        byte b0 = 0;
        byte b1 = 1;

        if (oworld.a(i - 1, j, k) == this.ca || oworld.a(i + 1, j, k) == this.ca) {
            b0 = 1;
            b1 = 0;
        }

        int i1;

        for (i1 = j; oworld.a(i, i1 - 1, k) == this.ca; --i1) {
            ;
        }

        if (oworld.a(i, i1 - 1, k) != OBlock.ap.ca) {
            oworld.e(i, j, k, 0);
        } else {
            int j1;

            for (j1 = 1; j1 < 4 && oworld.a(i, i1 + j1, k) == this.ca; ++j1) {
                ;
            }

            if (j1 == 3 && oworld.a(i, i1 + j1, k) == OBlock.ap.ca) {
                boolean flag = oworld.a(i - 1, j, k) == this.ca || oworld.a(i + 1, j, k) == this.ca;
                boolean flag1 = oworld.a(i, j, k - 1) == this.ca || oworld.a(i, j, k + 1) == this.ca;

                if (flag && flag1) {
                    oworld.e(i, j, k, 0);
                } else {
                    if ((oworld.a(i + b0, j, k + b1) != OBlock.ap.ca || oworld.a(i - b0, j, k - b1) != this.ca) && (oworld.a(i - b0, j, k - b1) != OBlock.ap.ca || oworld.a(i + b0, j, k + b1) != this.ca)) {
                        oworld.e(i, j, k, 0);
                    }
                }
            } else {
                oworld.e(i, j, k, 0);
            }
        }
    }

    public int a(Random random) {
        return 0;
    }

    public void a(OWorld oworld, int i, int j, int k, OEntity oentity) {
        if (oentity.o == null && oentity.n == null) {
            oentity.aa();
        }

    }
}
