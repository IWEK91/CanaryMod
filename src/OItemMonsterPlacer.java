public class OItemMonsterPlacer extends OItem {

    public OItemMonsterPlacer(int i) {
        super(i);
        this.a(true);
        this.a(OCreativeTabs.f);
    }

    public String l(OItemStack oitemstack) {
        String s = ("" + OStatCollector.a(this.a() + ".name")).trim();
        String s1 = OEntityList.b(oitemstack.j());

        if (s1 != null) {
            s = s + " " + OStatCollector.a("entity." + s1 + ".name");
        }

        return s;
    }

    public boolean a(OItemStack oitemstack, OEntityPlayer oentityplayer, OWorld oworld, int i, int j, int k, int l, float f, float f1, float f2) {
        // CanaryMod: call onItemUse
        if (oworld.I || (Boolean) etc.getLoader().callHook(PluginLoader.Hook.ITEM_USE, ((OEntityPlayerMP) oentityplayer).getPlayer(), this.getBlockInfo(oworld, i, j, k, l), null, ((OEntityPlayerMP) oentityplayer).getPlayer().getItemStackInHand())) {
            return true;
        } else {
            int i1 = oworld.a(i, j, k);

            i += OFacing.b[l];
            j += OFacing.c[l];
            k += OFacing.d[l];
            double d0 = 0.0D;

            if (l == 1 && OBlock.p[i1] != null && OBlock.p[i1].d() == 11) {
                d0 = 0.5D;
            }

            if (a(oworld, oitemstack.j(), (double) i + 0.5D, (double) j + d0, (double) k + 0.5D) != null && !oentityplayer.cd.d) {
                --oitemstack.a;
            }

            return true;
        }
    }

    public static OEntity a(OWorld oworld, int i, double d0, double d1, double d2, boolean spawn) { //CanaryMod: added option to not spawn entities
        if (!OEntityList.a.containsKey(Integer.valueOf(i))) {
            return null;
        } else {
            OEntity oentity = null;

            for (int j = 0; j < 1; ++j) {
                oentity = OEntityList.a(i, oworld);
                if (oentity != null && oentity instanceof OEntityLiving) {
                    OEntityLiving oentityliving = (OEntityLiving) oentity;

                    oentity.b(d0, d1, d2, OMathHelper.g(oworld.t.nextFloat() * 360.0F), 0.0F);
                    oentityliving.az = oentityliving.z;
                    oentityliving.ax = oentityliving.z;
                    oentityliving.bG();
                    if (spawn) {
                        oworld.d(oentity);
                    }
                    oentityliving.aO();
                }
            }

            return oentity;
        }
    }

    public static OEntity a(OWorld oworld, int i, double d0, double d1, double d2) {
        return a(oworld, i, d0, d1, d2, true);
    }
}
