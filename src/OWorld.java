import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

public abstract class OWorld implements OIBlockAccess {

    public boolean d = false;
    public List e = new ArrayList();
    protected List f = new ArrayList();
    public List g = new ArrayList();
    private List a = new ArrayList();
    private List b = new ArrayList();
    public List h = new ArrayList();
    public List i = new ArrayList();
    private long c = 16777215L;
    public int j = 0;
    protected int k = (new Random()).nextInt();
    protected final int l = 1013904223;
    protected float m;
    protected float n;
    protected float o;
    protected float p;
    public int q = 0;
    public boolean r = false;
    public int s;
    public Random t = new Random();
    public final OWorldProvider u;
    protected List v = new ArrayList();
    protected OIChunkProvider w;
    protected final OISaveHandler x;
    protected OWorldInfo y;
    public boolean z;
    public OMapStorage A;
    public final OVillageCollection B;
    protected final OVillageSiege C = new OVillageSiege(this);
    public final OProfiler D;
    private final OVec3Pool J = new OVec3Pool(300, 2000);
    private final Calendar K = Calendar.getInstance();
    private ArrayList L = new ArrayList();
    private boolean M;
    protected boolean E = true;
    protected boolean F = true;
    protected Set G = new HashSet();
    private int N;
    int[] H;
    // private List O; // CanaryMod: Fix ConcurrentModificationException
    public boolean I;

    // CanaryMod
    public final World world = new World((OWorldServer) this);
    boolean loadedpreload = false;
    public final String name;

    public OBiomeGenBase a(int i, int j) {
        if (this.f(i, 0, j)) {
            OChunk ochunk = this.d(i, j);

            if (ochunk != null) {
                return ochunk.a(i & 15, j & 15, this.u.d);
            }
        }

        return this.u.d.a(i, j);
    }

    public OWorldChunkManager t() {
        return this.u.d;
    }

    public OWorld(OISaveHandler oisavehandler, String s, OWorldSettings oworldsettings, OWorldProvider oworldprovider, OProfiler oprofiler) {
        this.N = this.t.nextInt(12000);
        this.H = new int['\u8000'];
        //this.O = new ArrayList(); // CanaryMod: Fix ConcurrentModificationException
        this.I = false;
        this.x = oisavehandler;
        this.D = oprofiler;
        this.A = new OMapStorage(oisavehandler);
        this.y = oisavehandler.d();
        if (oworldprovider != null) {
            this.u = oworldprovider;
        } else if (this.y != null && this.y.j() != 0) {
            this.u = OWorldProvider.a(this.y.j());
        } else {
            this.u = OWorldProvider.a(0);
        }

        if (this.y == null) {
            this.y = new OWorldInfo(oworldsettings, s);
        } else {
            this.y.a(s);
        }

        this.u.a(this);
        this.w = this.j();
        if (!this.y.w()) {
            try {
                this.a(oworldsettings);
            } catch (Throwable throwable) {
                OCrashReport ocrashreport = OCrashReport.a(throwable, "Exception initializing level");

                try {
                    this.a(ocrashreport);
                } catch (Throwable throwable1) {
                    ;
                }

                throw new OReportedException(ocrashreport);
            }

            this.y.d(true);
        }

        OVillageCollection ovillagecollection = (OVillageCollection) this.A.a(OVillageCollection.class, "villages");

        if (ovillagecollection == null) {
            this.B = new OVillageCollection(this);
            this.A.a("villages", (OWorldSavedData) this.B);
        } else {
            this.B = ovillagecollection;
            this.B.a(this);
        }

        this.x();
        this.a();

        this.name = s; // CanaryMod: store world name in an accessible place.
    }

    protected abstract OIChunkProvider j();

    protected void a(OWorldSettings oworldsettings) {
        this.y.d(true);
    }

    public int b(int i, int j) {
        int k;

        for (k = 63; !this.c(i, k + 1, j); ++k) {
            ;
        }

        return this.a(i, k, j);
    }

    public int a(int i, int j, int k) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return 0;
            } else if (j >= 256) {
                return 0;
            } else {
                OChunk ochunk = null;

                try {
                    ochunk = this.e(i >> 4, k >> 4);
                    return ochunk.a(i & 15, j, k & 15);
                } catch (Throwable throwable) {
                    OCrashReport ocrashreport = OCrashReport.a(throwable, "Exception getting block type in world");
                    OCrashReportCategory ocrashreportcategory = ocrashreport.a("Requested block coordinates");

                    ocrashreportcategory.a("Found chunk", Boolean.valueOf(ochunk == null));
                    ocrashreportcategory.a("Location", OCrashReportCategory.a(i, j, k));
                    throw new OReportedException(ocrashreport);
                }
            }
        } else {
            return 0;
        }
    }

    public int b(int i, int j, int k) {
        return i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000 ? (j < 0 ? 0 : (j >= 256 ? 0 : this.e(i >> 4, k >> 4).b(i & 15, j, k & 15))) : 0;
    }

    public boolean c(int i, int j, int k) {
        return this.a(i, j, k) == 0;
    }

    public boolean d(int i, int j, int k) {
        int l = this.a(i, j, k);

        return OBlock.p[l] != null && OBlock.p[l].u();
    }

    public int e(int i, int j, int k) {
        int l = this.a(i, j, k);

        return OBlock.p[l] != null ? OBlock.p[l].d() : -1;
    }

    public boolean f(int i, int j, int k) {
        return j >= 0 && j < 256 ? this.c(i >> 4, k >> 4) : false;
    }

    public boolean a(int i, int j, int k, int l) {
        return this.d(i - l, j - l, k - l, i + l, j + l, k + l);
    }

    public boolean d(int i, int j, int k, int l, int i1, int j1) {
        if (i1 >= 0 && j < 256) {
            i >>= 4;
            k >>= 4;
            l >>= 4;
            j1 >>= 4;

            for (int k1 = i; k1 <= l; ++k1) {
                for (int l1 = k; l1 <= j1; ++l1) {
                    if (!this.c(k1, l1)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected boolean c(int i, int j) {
        return (this.w != null ? this.w.a(i, j) : false); // CanaryMod: fix NPE
    }

    public OChunk d(int i, int j) {
        return this.e(i >> 4, j >> 4);
    }

    public OChunk e(int i, int j) {
        return this.w.d(i, j);
    }

    public boolean c(int i, int j, int k, int l, int i1) {
        return this.a(i, j, k, l, i1, true);
    }

    public boolean a(int i, int j, int k, int l, int i1, boolean flag) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return false;
            } else if (j >= 256) {
                return false;
            } else {
                OChunk ochunk = this.e(i >> 4, k >> 4);
                boolean flag1 = false;
                // CanaryMod: Get Block Info
                Block block = this.world.getBlockAt(i, j, k);
                // CanaryMod ignore if new block is air
                if (l == 0 || !(Boolean) OEntity.manager.callHook(PluginLoader.Hook.BLOCK_UPDATE, block, l)) {
                    flag1 = ochunk.a(i & 15, j, k & 15, l, i1);
                }
                this.D.a("checkLight");
                this.z(i, j, k);
                this.D.b();
                if (flag && flag1 && (this.I || ochunk.o)) {
                    this.i(i, j, k);
                }

                return flag1;
            }
        } else {
            return false;
        }
    }

    public boolean b(int i, int j, int k, int l) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return false;
            } else if (j >= 256) {
                return false;
            } else {
                OChunk ochunk = this.e(i >> 4, k >> 4);
                // CanaryMod Block Updates
                // Get Block Info
                Block block = this.world.getBlockAt(i, j, k);
                // ignore if new block is air
                boolean flag = false;

                if (l == 0 || !(Boolean) OEntity.manager.callHook(PluginLoader.Hook.BLOCK_UPDATE, block, l)) {
                    flag = ochunk.a(i & 15, j, k & 15, l);
                }
                this.D.a("checkLight");
                this.z(i, j, k);
                this.D.b();
                if (flag && (this.I || ochunk.o)) {
                    this.i(i, j, k);
                }

                return flag;
            }
        } else {
            return false;
        }
    }

    public OMaterial g(int i, int j, int k) {
        int l = this.a(i, j, k);

        return l == 0 ? OMaterial.a : OBlock.p[l].cB;
    }

    public int h(int i, int j, int k) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return 0;
            } else if (j >= 256) {
                return 0;
            } else {
                OChunk ochunk = this.e(i >> 4, k >> 4);

                i &= 15;
                k &= 15;
                return ochunk.c(i, j, k);
            }
        } else {
            return 0;
        }
    }

    public void c(int i, int j, int k, int l) {
        if (this.d(i, j, k, l)) {
            this.f(i, j, k, this.a(i, j, k));
        }
    }

    public boolean d(int i, int j, int k, int l) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return false;
            } else if (j >= 256) {
                return false;
            } else {
                OChunk ochunk = this.e(i >> 4, k >> 4);
                int i1 = i & 15;
                int j1 = k & 15;
                boolean flag = ochunk.b(i1, j, j1, l);

                if (flag && (this.I || ochunk.o && OBlock.u[ochunk.a(i1, j, j1) & 4095])) {
                    this.i(i, j, k);
                }

                return flag;
            }
        } else {
            return false;
        }
    }

    public boolean e(int i, int j, int k, int l) {
        if (this.b(i, j, k, l)) {
            this.f(i, j, k, l);
            return true;
        } else {
            return false;
        }
    }

    public boolean d(int i, int j, int k, int l, int i1) {
        if (this.c(i, j, k, l, i1)) {
            this.f(i, j, k, l);
            return true;
        } else {
            return false;
        }
    }

    public void i(int i, int j, int k) {
        for (int l = 0; l < this.v.size(); ++l) {
            ((OIWorldAccess) this.v.get(l)).a(i, j, k);
        }
    }

    public void f(int i, int j, int k, int l) {
        this.h(i, j, k, l);
    }

    public void g(int i, int j, int k, int l) {
        int i1;

        if (k > l) {
            i1 = l;
            l = k;
            k = i1;
        }

        if (!this.u.f) {
            for (i1 = k; i1 <= l; ++i1) {
                this.c(OEnumSkyBlock.a, i, i1, j);
            }
        }

        this.e(i, k, j, i, l, j);
    }

    public void j(int i, int j, int k) {
        for (int l = 0; l < this.v.size(); ++l) {
            ((OIWorldAccess) this.v.get(l)).a(i, j, k, i, j, k);
        }
    }

    public void e(int i, int j, int k, int l, int i1, int j1) {
        for (int k1 = 0; k1 < this.v.size(); ++k1) {
            ((OIWorldAccess) this.v.get(k1)).a(i, j, k, l, i1, j1);
        }
    }

    public void h(int i, int j, int k, int l) {
        this.m(i - 1, j, k, l);
        this.m(i + 1, j, k, l);
        this.m(i, j - 1, k, l);
        this.m(i, j + 1, k, l);
        this.m(i, j, k - 1, l);
        this.m(i, j, k + 1, l);
    }

    private void m(int i, int j, int k, int l) {
        if (!this.r && !this.I) {
            int i1 = this.a(i, j, k);
            OBlock oblock = OBlock.p[i1];

            if (oblock != null) {
                try {
                    oblock.a(this, i, j, k, l);
                } catch (Throwable throwable) {
                    OCrashReport ocrashreport = OCrashReport.a(throwable, "Exception while updating neighbours");
                    OCrashReportCategory ocrashreportcategory = ocrashreport.a("Block being updated");

                    int j1;

                    try {
                        j1 = this.h(i, j, k);
                    } catch (Throwable throwable1) {
                        j1 = -1;
                    }

                    ocrashreportcategory.a("Source block type", (Callable) (new OCallableLvl1(this, l)));
                    OCrashReportCategory.a(ocrashreportcategory, i, j, k, i1, j1);
                    throw new OReportedException(ocrashreport);
                }
            }
        }
    }

    public boolean k(int i, int j, int k) {
        return this.e(i >> 4, k >> 4).d(i & 15, j, k & 15);
    }

    public int l(int i, int j, int k) {
        if (j < 0) {
            return 0;
        } else {
            if (j >= 256) {
                j = 255;
            }

            return this.e(i >> 4, k >> 4).c(i & 15, j, k & 15, 0);
        }
    }

    public int m(int i, int j, int k) {
        return this.a(i, j, k, true);
    }

    public int a(int i, int j, int k, boolean flag) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (flag) {
                int l = this.a(i, j, k);

                if (l == OBlock.an.cm || l == OBlock.bR.cm || l == OBlock.aD.cm || l == OBlock.aK.cm || l == OBlock.aw.cm) {
                    int i1 = this.a(i, j + 1, k, false);
                    int j1 = this.a(i + 1, j, k, false);
                    int k1 = this.a(i - 1, j, k, false);
                    int l1 = this.a(i, j, k + 1, false);
                    int i2 = this.a(i, j, k - 1, false);

                    if (j1 > i1) {
                        i1 = j1;
                    }

                    if (k1 > i1) {
                        i1 = k1;
                    }

                    if (l1 > i1) {
                        i1 = l1;
                    }

                    if (i2 > i1) {
                        i1 = i2;
                    }

                    return i1;
                }
            }

            if (j < 0) {
                return 0;
            } else {
                if (j >= 256) {
                    j = 255;
                }

                OChunk ochunk = this.e(i >> 4, k >> 4);

                i &= 15;
                k &= 15;
                return ochunk.c(i, j, k, this.j);
            }
        } else {
            return 15;
        }
    }

    public int f(int i, int j) {
        if (i >= -30000000 && j >= -30000000 && i < 30000000 && j < 30000000) {
            if (!this.c(i >> 4, j >> 4)) {
                return 0;
            } else {
                OChunk ochunk = this.e(i >> 4, j >> 4);

                return ochunk.b(i & 15, j & 15);
            }
        } else {
            return 0;
        }
    }

    public int g(int i, int j) {
        if (i >= -30000000 && j >= -30000000 && i < 30000000 && j < 30000000) {
            if (!this.c(i >> 4, j >> 4)) {
                return 0;
            } else {
                OChunk ochunk = this.e(i >> 4, j >> 4);

                return ochunk.p;
            }
        } else {
            return 0;
        }
    }

    public int b(OEnumSkyBlock oenumskyblock, int i, int j, int k) {
        if (j < 0) {
            j = 0;
        }

        if (j >= 256) {
            j = 255;
        }

        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            int l = i >> 4;
            int i1 = k >> 4;

            if (!this.c(l, i1)) {
                return oenumskyblock.c;
            } else {
                OChunk ochunk = this.e(l, i1);

                return ochunk.a(oenumskyblock, i & 15, j, k & 15);
            }
        } else {
            return oenumskyblock.c;
        }
    }

    public void b(OEnumSkyBlock oenumskyblock, int i, int j, int k, int l) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j >= 0) {
                if (j < 256) {
                    if (this.c(i >> 4, k >> 4)) {
                        OChunk ochunk = this.e(i >> 4, k >> 4);

                        ochunk.a(oenumskyblock, i & 15, j, k & 15, l);
                        etc.getLoader().callHook(PluginLoader.Hook.LIGHT_CHANGE, i, j, k, l); //CanaryMod: fire light change hook

                        for (int i1 = 0; i1 < this.v.size(); ++i1) {
                            ((OIWorldAccess) this.v.get(i1)).b(i, j, k);
                        }
                    }
                }
            }
        }
    }

    public void o(int i, int j, int k) {
        for (int l = 0; l < this.v.size(); ++l) {
            ((OIWorldAccess) this.v.get(l)).b(i, j, k);
        }
    }

    public float p(int i, int j, int k) {
        return this.u.g[this.m(i, j, k)];
    }

    public boolean u() {
        return this.j < 4;
    }

    public OMovingObjectPosition a(OVec3 ovec3, OVec3 ovec31) {
        return this.a(ovec3, ovec31, false, false);
    }

    public OMovingObjectPosition a(OVec3 ovec3, OVec3 ovec31, boolean flag) {
        return this.a(ovec3, ovec31, flag, false);
    }

    public OMovingObjectPosition a(OVec3 ovec3, OVec3 ovec31, boolean flag, boolean flag1) {
        if (!Double.isNaN(ovec3.c) && !Double.isNaN(ovec3.d) && !Double.isNaN(ovec3.e)) {
            if (!Double.isNaN(ovec31.c) && !Double.isNaN(ovec31.d) && !Double.isNaN(ovec31.e)) {
                int i = OMathHelper.c(ovec31.c);
                int j = OMathHelper.c(ovec31.d);
                int k = OMathHelper.c(ovec31.e);
                int l = OMathHelper.c(ovec3.c);
                int i1 = OMathHelper.c(ovec3.d);
                int j1 = OMathHelper.c(ovec3.e);
                int k1 = this.a(l, i1, j1);
                int l1 = this.h(l, i1, j1);
                OBlock oblock = OBlock.p[k1];

                if ((!flag1 || oblock == null || oblock.e(this, l, i1, j1) != null) && k1 > 0 && oblock.a(l1, flag)) {
                    OMovingObjectPosition omovingobjectposition = oblock.a(this, l, i1, j1, ovec3, ovec31);

                    if (omovingobjectposition != null) {
                        return omovingobjectposition;
                    }
                }

                k1 = 200;

                while (k1-- >= 0) {
                    if (Double.isNaN(ovec3.c) || Double.isNaN(ovec3.d) || Double.isNaN(ovec3.e)) {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k) {
                        return null;
                    }

                    boolean flag2 = true;
                    boolean flag3 = true;
                    boolean flag4 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l) {
                        d0 = (double) l + 1.0D;
                    } else if (i < l) {
                        d0 = (double) l + 0.0D;
                    } else {
                        flag2 = false;
                    }

                    if (j > i1) {
                        d1 = (double) i1 + 1.0D;
                    } else if (j < i1) {
                        d1 = (double) i1 + 0.0D;
                    } else {
                        flag3 = false;
                    }

                    if (k > j1) {
                        d2 = (double) j1 + 1.0D;
                    } else if (k < j1) {
                        d2 = (double) j1 + 0.0D;
                    } else {
                        flag4 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = ovec31.c - ovec3.c;
                    double d7 = ovec31.d - ovec3.d;
                    double d8 = ovec31.e - ovec3.e;

                    if (flag2) {
                        d3 = (d0 - ovec3.c) / d6;
                    }

                    if (flag3) {
                        d4 = (d1 - ovec3.d) / d7;
                    }

                    if (flag4) {
                        d5 = (d2 - ovec3.e) / d8;
                    }

                    boolean flag5 = false;
                    byte b0;

                    if (d3 < d4 && d3 < d5) {
                        if (i > l) {
                            b0 = 4;
                        } else {
                            b0 = 5;
                        }

                        ovec3.c = d0;
                        ovec3.d += d7 * d3;
                        ovec3.e += d8 * d3;
                    } else if (d4 < d5) {
                        if (j > i1) {
                            b0 = 0;
                        } else {
                            b0 = 1;
                        }

                        ovec3.c += d6 * d4;
                        ovec3.d = d1;
                        ovec3.e += d8 * d4;
                    } else {
                        if (k > j1) {
                            b0 = 2;
                        } else {
                            b0 = 3;
                        }

                        ovec3.c += d6 * d5;
                        ovec3.d += d7 * d5;
                        ovec3.e = d2;
                    }

                    OVec3 ovec32 = this.S().a(ovec3.c, ovec3.d, ovec3.e);

                    l = (int) (ovec32.c = (double) OMathHelper.c(ovec3.c));
                    if (b0 == 5) {
                        --l;
                        ++ovec32.c;
                    }

                    i1 = (int) (ovec32.d = (double) OMathHelper.c(ovec3.d));
                    if (b0 == 1) {
                        --i1;
                        ++ovec32.d;
                    }

                    j1 = (int) (ovec32.e = (double) OMathHelper.c(ovec3.e));
                    if (b0 == 3) {
                        --j1;
                        ++ovec32.e;
                    }

                    int i2 = this.a(l, i1, j1);
                    int j2 = this.h(l, i1, j1);
                    OBlock oblock1 = OBlock.p[i2];

                    if ((!flag1 || oblock1 == null || oblock1.e(this, l, i1, j1) != null) && i2 > 0 && oblock1.a(j2, flag)) {
                        OMovingObjectPosition omovingobjectposition1 = oblock1.a(this, l, i1, j1, ovec3, ovec31);

                        if (omovingobjectposition1 != null) {
                            return omovingobjectposition1;
                        }
                    }
                }

                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void a(OEntity oentity, String s, float f, float f1) {
        if (oentity != null && s != null) {
            for (int i = 0; i < this.v.size(); ++i) {
                ((OIWorldAccess) this.v.get(i)).a(s, oentity.t, oentity.u - (double) oentity.M, oentity.v, f, f1);
            }
        }
    }

    public void a(OEntityPlayer oentityplayer, String s, float f, float f1) {
        if (oentityplayer != null && s != null) {
            for (int i = 0; i < this.v.size(); ++i) {
                ((OIWorldAccess) this.v.get(i)).a(oentityplayer, s, oentityplayer.t, oentityplayer.u - (double) oentityplayer.M, oentityplayer.v, f, f1);
            }
        }
    }

    public void a(double d0, double d1, double d2, String s, float f, float f1) {
        if (s != null) {
            for (int i = 0; i < this.v.size(); ++i) {
                ((OIWorldAccess) this.v.get(i)).a(s, d0, d1, d2, f, f1);
            }
        }
    }

    public void a(double d0, double d1, double d2, String s, float f, float f1, boolean flag) {}

    public void a(String s, int i, int j, int k) {
        for (int l = 0; l < this.v.size(); ++l) {
            ((OIWorldAccess) this.v.get(l)).a(s, i, j, k);
        }
    }

    public void a(String s, double d0, double d1, double d2, double d3, double d4, double d5) {
        for (int i = 0; i < this.v.size(); ++i) {
            ((OIWorldAccess) this.v.get(i)).a(s, d0, d1, d2, d3, d4, d5);
        }
    }

    public boolean c(OEntity oentity) {
        this.i.add(oentity);
        return true;
    }

    public boolean d(OEntity oentity) {
        // CanaryMod: mob spawn hook
        if (oentity instanceof OEntityLiving && !(oentity instanceof OEntityPlayer)) {
            if ((etc.getInstance().getMobSpawnRate() < 100 && etc.getInstance().getMobSpawnRate() > 0 && etc.getInstance().getMobSpawnRate() <= this.t.nextInt(101)) || etc.getInstance().getMobSpawnRate() <= 0 || (Boolean) (etc.getLoader().callHook(PluginLoader.Hook.MOB_SPAWN, new Mob((OEntityLiving) oentity)))) {
                return false;
            }
        }

        int i = OMathHelper.c(oentity.t / 16.0D);
        int j = OMathHelper.c(oentity.v / 16.0D);
        boolean flag = false;

        if (oentity instanceof OEntityPlayer) {
            flag = true;
        }

        if (!flag && !this.c(i, j)) {
            return false;
        } else {
            if (oentity instanceof OEntityPlayer) {
                OEntityPlayer oentityplayer = (OEntityPlayer) oentity;

                this.h.add(oentityplayer);
                this.c();
            }

            this.e(i, j).a(oentity);
            this.e.add(oentity);
            this.a(oentity);
            return true;
        }
    }

    protected void a(OEntity oentity) {
        for (int i = 0; i < this.v.size(); ++i) {
            ((OIWorldAccess) this.v.get(i)).a(oentity);
        }
    }

    protected void b(OEntity oentity) {
        for (int i = 0; i < this.v.size(); ++i) {
            ((OIWorldAccess) this.v.get(i)).b(oentity);
        }
    }

    public void e(OEntity oentity) {
        if (oentity.n != null) {
            oentity.n.a((OEntity) null);
        }

        if (oentity.o != null) {
            oentity.a((OEntity) null);
        }

        oentity.x();
        if (oentity instanceof OEntityPlayer) {
            this.h.remove(oentity);
            this.c();
        }
    }

    public void f(OEntity oentity) {
        oentity.x();
        if (oentity instanceof OEntityPlayer) {
            this.h.remove(oentity);
            this.c();
        }

        int i = oentity.ai;
        int j = oentity.ak;

        if (oentity.ah && this.c(i, j)) {
            this.e(i, j).b(oentity);
        }

        this.e.remove(oentity);
        this.b(oentity);
    }

    public void a(OIWorldAccess oiworldaccess) {
        this.v.add(oiworldaccess);
    }

    public List a(OEntity oentity, OAxisAlignedBB oaxisalignedbb) {
        this.L.clear();
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = i1; l1 < j1; ++l1) {
                if (this.f(k1, 64, l1)) {
                    for (int i2 = k - 1; i2 < l; ++i2) {
                        OBlock oblock = OBlock.p[this.a(k1, i2, l1)];

                        if (oblock != null) {
                            oblock.a(this, k1, i2, l1, oaxisalignedbb, this.L, oentity);
                        }
                    }
                }
            }
        }

        // CanaryMod: Implemented fix via M4411K4 VEHICLE_COLLISION hook
        BaseVehicle vehicle = null;

        if (oentity instanceof OEntityMinecart) {
            vehicle = ((OEntityMinecart) oentity).cart;
        } else if (oentity instanceof OEntityBoat) {
            vehicle = ((OEntityBoat) oentity).boat;
        }

        double d0 = 0.25D;
        List list = this.b(oentity, oaxisalignedbb.b(d0, d0, d0));

        for (int j2 = 0; j2 < list.size(); ++j2) {
            OEntity oentity1 = (OEntity) list.get(j2); // CanaryMod: split these two lines
            OAxisAlignedBB oaxisalignedbb1 = oentity1.E();

            if (oaxisalignedbb1 != null && oaxisalignedbb1.a(oaxisalignedbb)) {
                 // CanaryMod: this collided with a boat
                if (vehicle != null && (Boolean) etc.getLoader().callHook(PluginLoader.Hook.VEHICLE_COLLISION, vehicle, oentity1.entity)) {
                    continue;
                }
                // CanaryMod: End
                this.L.add(oaxisalignedbb1);
            }

            oaxisalignedbb1 = oentity.g((OEntity) list.get(j2));
            if (oaxisalignedbb1 != null && oaxisalignedbb1.a(oaxisalignedbb)) {
                // CanaryMod: this collided with entity
                if (vehicle != null && (Boolean) etc.getLoader().callHook(PluginLoader.Hook.VEHICLE_COLLISION, vehicle, oentity1.entity)) {
                    continue;
                }
                // CanaryMod: End
                this.L.add(oaxisalignedbb1);
            }
        }

        return this.L;
    }

    public List a(OAxisAlignedBB oaxisalignedbb) {
        this.L.clear();
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = i1; l1 < j1; ++l1) {
                if (this.f(k1, 64, l1)) {
                    for (int i2 = k - 1; i2 < l; ++i2) {
                        OBlock oblock = OBlock.p[this.a(k1, i2, l1)];

                        if (oblock != null) {
                            oblock.a(this, k1, i2, l1, oaxisalignedbb, this.L, (OEntity) null);
                        }
                    }
                }
            }
        }

        return this.L;
    }

    public int a(float f) {
        float f1 = this.c(f);
        float f2 = 1.0F - (OMathHelper.b(f1 * 3.1415927F * 2.0F) * 2.0F + 0.5F);

        if (f2 < 0.0F) {
            f2 = 0.0F;
        }

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        f2 = 1.0F - f2;
        f2 = (float) ((double) f2 * (1.0D - (double) (this.j(f) * 5.0F) / 16.0D));
        f2 = (float) ((double) f2 * (1.0D - (double) (this.i(f) * 5.0F) / 16.0D));
        f2 = 1.0F - f2;
        return (int) (f2 * 11.0F);
    }

    public float c(float f) {
        return this.u.a(this.y.g(), f);
    }

    public int h(int i, int j) {
        return this.d(i, j).d(i & 15, j & 15);
    }

    public int i(int i, int j) {
        OChunk ochunk = this.d(i, j);
        int k = ochunk.h() + 15;

        i &= 15;

        for (j &= 15; k > 0; --k) {
            int l = ochunk.a(i, k, j);

            if (l != 0 && OBlock.p[l].cB.c() && OBlock.p[l].cB != OMaterial.j) {
                return k + 1;
            }
        }

        return -1;
    }

    public void a(int i, int j, int k, int l, int i1) {}

    public void a(int i, int j, int k, int l, int i1, int j1) {}

    public void b(int i, int j, int k, int l, int i1) {}

    public void h() {
        this.D.a("entities");
        this.D.a("global");

        int i;
        OEntity oentity;
        OCrashReport ocrashreport;
        OCrashReportCategory ocrashreportcategory;

        for (i = 0; i < this.i.size(); ++i) {
            oentity = (OEntity) this.i.get(i);

            try {
                ++oentity.ab;
                oentity.j_();
            } catch (Throwable throwable) {
                ocrashreport = OCrashReport.a(throwable, "Ticking entity");
                ocrashreportcategory = ocrashreport.a("Entity being ticked");
                if (oentity == null) {
                    ocrashreportcategory.a("Entity", "~~NULL~~");
                } else {
                    oentity.a(ocrashreportcategory);
                }

                throw new OReportedException(ocrashreport);
            }

            if (oentity.L) {
                this.i.remove(i--);
            }
        }

        this.D.c("remove");
        this.e.removeAll(this.f);

        int j;
        int k;

        for (i = 0; i < this.f.size(); ++i) {
            oentity = (OEntity) this.f.get(i);
            j = oentity.ai;
            k = oentity.ak;
            if (oentity.ah && this.c(j, k)) {
                this.e(j, k).b(oentity);
            }
        }

        for (i = 0; i < this.f.size(); ++i) {
            this.b((OEntity) this.f.get(i));
        }

        this.f.clear();
        this.D.c("regular");

        for (i = 0; i < this.e.size(); ++i) {
            oentity = (OEntity) this.e.get(i);
            if (oentity.o != null) {
                if (!oentity.o.L && oentity.o.n == oentity) {
                    continue;
                }

                oentity.o.n = null;
                oentity.o = null;
            }

            this.D.a("tick");
            if (!oentity.L) {
                try {
                    this.g(oentity);
                } catch (Throwable throwable1) {
                    ocrashreport = OCrashReport.a(throwable1, "Ticking entity");
                    ocrashreportcategory = ocrashreport.a("Entity being ticked");
                    if (oentity == null) {
                        ocrashreportcategory.a("Entity", "~~NULL~~");
                    } else {
                        oentity.a(ocrashreportcategory);
                    }

                    throw new OReportedException(ocrashreport);
                }
            }

            this.D.b();
            this.D.a("remove");
            if (oentity.L) {
                j = oentity.ai;
                k = oentity.ak;
                if (oentity.ah && this.c(j, k)) {
                    this.e(j, k).b(oentity);
                }

                this.e.remove(i--);
                this.b(oentity);
            }

            this.D.b();
        }

        this.D.c("tileEntities");
        this.M = true;
        Iterator iterator = this.g.iterator();

        while (iterator.hasNext()) {
            OTileEntity otileentity = (OTileEntity) iterator.next();

            if (!otileentity.r() && otileentity.o() && this.f(otileentity.l, otileentity.m, otileentity.n)) {
                try {
                    otileentity.g();
                } catch (Throwable throwable2) {
                    ocrashreport = OCrashReport.a(throwable2, "Ticking tile entity");
                    ocrashreportcategory = ocrashreport.a("Tile entity being ticked");
                    if (otileentity == null) {
                        ocrashreportcategory.a("Tile entity", "~~NULL~~");
                    } else {
                        otileentity.a(ocrashreportcategory);
                    }

                    throw new OReportedException(ocrashreport);
                }
            }

            if (otileentity.r()) {
                iterator.remove();
                if (this.c(otileentity.l >> 4, otileentity.n >> 4)) {
                    OChunk ochunk = this.e(otileentity.l >> 4, otileentity.n >> 4);

                    if (ochunk != null) {
                        ochunk.f(otileentity.l & 15, otileentity.m, otileentity.n & 15);
                    }
                }
            }
        }

        this.M = false;
        if (!this.b.isEmpty()) {
            this.g.removeAll(this.b);
            this.b.clear();
        }

        this.D.c("pendingTileEntities");
        if (!this.a.isEmpty()) {
            for (int l = 0; l < this.a.size(); ++l) {
                OTileEntity otileentity1 = (OTileEntity) this.a.get(l);

                if (!otileentity1.r()) {
                    if (!this.g.contains(otileentity1)) {
                        this.g.add(otileentity1);
                    }

                    if (this.c(otileentity1.l >> 4, otileentity1.n >> 4)) {
                        OChunk ochunk1 = this.e(otileentity1.l >> 4, otileentity1.n >> 4);

                        if (ochunk1 != null) {
                            ochunk1.a(otileentity1.l & 15, otileentity1.m, otileentity1.n & 15, otileentity1);
                        }
                    }

                    this.i(otileentity1.l, otileentity1.m, otileentity1.n);
                }
            }

            this.a.clear();
        }

        this.D.b();
        this.D.b();
    }

    public void a(Collection collection) {
        if (this.M) {
            this.a.addAll(collection);
        } else {
            this.g.addAll(collection);
        }
    }

    public void g(OEntity oentity) {
        this.a(oentity, true);
    }

    public void a(OEntity oentity, boolean flag) {
        int i = OMathHelper.c(oentity.t);
        int j = OMathHelper.c(oentity.v);
        byte b0 = 32;

        if (!flag || this.d(i - b0, 0, j - b0, i + b0, 0, j + b0)) {
            oentity.T = oentity.t;
            oentity.U = oentity.u;
            oentity.V = oentity.v;
            oentity.B = oentity.z;
            oentity.C = oentity.A;
            if (flag && oentity.ah) {
                if (oentity.o != null) {
                    oentity.U();
                } else {
                    ++oentity.ab;
                    oentity.j_();
                }
            }

            this.D.a("chunkCheck");
            if (Double.isNaN(oentity.t) || Double.isInfinite(oentity.t)) {
                oentity.t = oentity.T;
            }

            if (Double.isNaN(oentity.u) || Double.isInfinite(oentity.u)) {
                oentity.u = oentity.U;
            }

            if (Double.isNaN(oentity.v) || Double.isInfinite(oentity.v)) {
                oentity.v = oentity.V;
            }

            if (Double.isNaN((double) oentity.A) || Double.isInfinite((double) oentity.A)) {
                oentity.A = oentity.C;
            }

            if (Double.isNaN((double) oentity.z) || Double.isInfinite((double) oentity.z)) {
                oentity.z = oentity.B;
            }

            int k = OMathHelper.c(oentity.t / 16.0D);
            int l = OMathHelper.c(oentity.u / 16.0D);
            int i1 = OMathHelper.c(oentity.v / 16.0D);

            if (!oentity.ah || oentity.ai != k || oentity.aj != l || oentity.ak != i1) {
                if (oentity.ah && this.c(oentity.ai, oentity.ak)) {
                    this.e(oentity.ai, oentity.ak).a(oentity, oentity.aj);
                }

                if (this.c(k, i1)) {
                    oentity.ah = true;
                    this.e(k, i1).a(oentity);
                } else {
                    oentity.ah = false;
                }
            }

            this.D.b();
            if (flag && oentity.ah && oentity.n != null) {
                if (!oentity.n.L && oentity.n.o == oentity) {
                    this.g(oentity.n);
                } else {
                    oentity.n.o = null;
                    oentity.n = null;
                }
            }
        }
    }

    public boolean b(OAxisAlignedBB oaxisalignedbb) {
        return this.a(oaxisalignedbb, (OEntity) null);
    }

    public boolean a(OAxisAlignedBB oaxisalignedbb, OEntity oentity) {
        List list = this.b((OEntity) null, oaxisalignedbb);

        for (int i = 0; i < list.size(); ++i) {
            OEntity oentity1 = (OEntity) list.get(i);

            if (!oentity1.L && oentity1.m && oentity1 != oentity) {
                return false;
            }
        }

        return true;
    }

    public boolean c(OAxisAlignedBB oaxisalignedbb) {
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        if (oaxisalignedbb.a < 0.0D) {
            --i;
        }

        if (oaxisalignedbb.b < 0.0D) {
            --k;
        }

        if (oaxisalignedbb.c < 0.0D) {
            --i1;
        }

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    OBlock oblock = OBlock.p[this.a(k1, l1, i2)];

                    if (oblock != null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean d(OAxisAlignedBB oaxisalignedbb) {
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        if (oaxisalignedbb.a < 0.0D) {
            --i;
        }

        if (oaxisalignedbb.b < 0.0D) {
            --k;
        }

        if (oaxisalignedbb.c < 0.0D) {
            --i1;
        }

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    OBlock oblock = OBlock.p[this.a(k1, l1, i2)];

                    if (oblock != null && oblock.cB.d()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean e(OAxisAlignedBB oaxisalignedbb) {
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        if (this.d(i, k, i1, j, l, j1)) {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        int j2 = this.a(k1, l1, i2);

                        if (j2 == OBlock.au.cm || j2 == OBlock.F.cm || j2 == OBlock.G.cm) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean a(OAxisAlignedBB oaxisalignedbb, OMaterial omaterial, OEntity oentity) {
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        if (!this.d(i, k, i1, j, l, j1)) {
            return false;
        } else {
            boolean flag = false;
            OVec3 ovec3 = this.S().a(0.0D, 0.0D, 0.0D);

            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        OBlock oblock = OBlock.p[this.a(k1, l1, i2)];

                        if (oblock != null && oblock.cB == omaterial) {
                            double d0 = (double) ((float) (l1 + 1) - OBlockFluid.e(this.h(k1, l1, i2)));

                            if ((double) l >= d0) {
                                flag = true;
                                oblock.a(this, k1, l1, i2, oentity, ovec3);
                            }
                        }
                    }
                }
            }

            if (ovec3.b() > 0.0D) {
                ovec3 = ovec3.a();
                double d1 = 0.014D;

                oentity.w += ovec3.c * d1;
                oentity.x += ovec3.d * d1;
                oentity.y += ovec3.e * d1;
            }

            return flag;
        }
    }

    public boolean a(OAxisAlignedBB oaxisalignedbb, OMaterial omaterial) {
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    OBlock oblock = OBlock.p[this.a(k1, l1, i2)];

                    if (oblock != null && oblock.cB == omaterial) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean b(OAxisAlignedBB oaxisalignedbb, OMaterial omaterial) {
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    OBlock oblock = OBlock.p[this.a(k1, l1, i2)];

                    if (oblock != null && oblock.cB == omaterial) {
                        int j2 = this.h(k1, l1, i2);
                        double d0 = (double) (l1 + 1);

                        if (j2 < 8) {
                            d0 = (double) (l1 + 1) - (double) j2 / 8.0D;
                        }

                        if (d0 >= oaxisalignedbb.b) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public OExplosion a(OEntity oentity, double d0, double d1, double d2, float f, boolean flag) {
        return this.a(oentity, d0, d1, d2, f, false, flag);
    }

    public OExplosion a(OEntity oentity, double d0, double d1, double d2, float f, boolean flag, boolean flag1) {
        OExplosion oexplosion = new OExplosion(this, oentity, d0, d1, d2, f);

        oexplosion.a = flag;
        oexplosion.b = flag1;
        oexplosion.a();
        oexplosion.a(true);
        return oexplosion;
    }

    public float a(OVec3 ovec3, OAxisAlignedBB oaxisalignedbb) {
        double d0 = 1.0D / ((oaxisalignedbb.d - oaxisalignedbb.a) * 2.0D + 1.0D);
        double d1 = 1.0D / ((oaxisalignedbb.e - oaxisalignedbb.b) * 2.0D + 1.0D);
        double d2 = 1.0D / ((oaxisalignedbb.f - oaxisalignedbb.c) * 2.0D + 1.0D);
        int i = 0;
        int j = 0;

        for (float f = 0.0F; f <= 1.0F; f = (float) ((double) f + d0)) {
            for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) ((double) f1 + d1)) {
                for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) ((double) f2 + d2)) {
                    double d3 = oaxisalignedbb.a + (oaxisalignedbb.d - oaxisalignedbb.a) * (double) f;
                    double d4 = oaxisalignedbb.b + (oaxisalignedbb.e - oaxisalignedbb.b) * (double) f1;
                    double d5 = oaxisalignedbb.c + (oaxisalignedbb.f - oaxisalignedbb.c) * (double) f2;

                    if (this.a(this.S().a(d3, d4, d5), ovec3) == null) {
                        ++i;
                    }

                    ++j;
                }
            }
        }

        return (float) i / (float) j;
    }

    public boolean a(OEntityPlayer oentityplayer, int i, int j, int k, int l) {
        if (l == 0) {
            --j;
        }

        if (l == 1) {
            ++j;
        }

        if (l == 2) {
            --k;
        }

        if (l == 3) {
            ++k;
        }

        if (l == 4) {
            --i;
        }

        if (l == 5) {
            ++i;
        }

        if (this.a(i, j, k) == OBlock.au.cm) {
            this.a(oentityplayer, 1004, i, j, k, 0);
            this.e(i, j, k, 0);
            return true;
        } else {
            return false;
        }
    }

    public OTileEntity q(int i, int j, int k) {
        if (j >= 256) {
            return null;
        } else {
            OChunk ochunk = this.e(i >> 4, k >> 4);

            if (ochunk == null) {
                return null;
            } else {
                OTileEntity otileentity = ochunk.e(i & 15, j, k & 15);

                if (otileentity == null) {
                    for (int l = 0; l < this.a.size(); ++l) {
                        OTileEntity otileentity1 = (OTileEntity) this.a.get(l);

                        if (!otileentity1.r() && otileentity1.l == i && otileentity1.m == j && otileentity1.n == k) {
                            otileentity = otileentity1;
                            break;
                        }
                    }
                }

                return otileentity;
            }
        }
    }

    public void a(int i, int j, int k, OTileEntity otileentity) {
        if (otileentity != null && !otileentity.r()) {
            if (this.M) {
                otileentity.l = i;
                otileentity.m = j;
                otileentity.n = k;
                this.a.add(otileentity);
            } else {
                this.g.add(otileentity);
                OChunk ochunk = this.e(i >> 4, k >> 4);

                if (ochunk != null) {
                    ochunk.a(i & 15, j, k & 15, otileentity);
                }
            }
        }
    }

    public void r(int i, int j, int k) {
        OTileEntity otileentity = this.q(i, j, k);

        if (otileentity != null && this.M) {
            otileentity.w_();
            this.a.remove(otileentity);
        } else {
            if (otileentity != null) {
                this.a.remove(otileentity);
                this.g.remove(otileentity);
            }

            OChunk ochunk = this.e(i >> 4, k >> 4);

            if (ochunk != null) {
                ochunk.f(i & 15, j, k & 15);
            }
        }
    }

    public void a(OTileEntity otileentity) {
        this.b.add(otileentity);
    }

    public boolean s(int i, int j, int k) {
        OBlock oblock = OBlock.p[this.a(i, j, k)];

        return oblock == null ? false : oblock.c();
    }

    public boolean t(int i, int j, int k) {
        return OBlock.i(this.a(i, j, k));
    }

    public boolean u(int i, int j, int k) {
        int l = this.a(i, j, k);

        if (l != 0 && OBlock.p[l] != null) {
            OAxisAlignedBB oaxisalignedbb = OBlock.p[l].e(this, i, j, k);

            return oaxisalignedbb != null && oaxisalignedbb.b() >= 1.0D;
        } else {
            return false;
        }
    }

    public boolean v(int i, int j, int k) {
        OBlock oblock = OBlock.p[this.a(i, j, k)];

        return oblock == null ? false : (oblock.cB.k() && oblock.b() ? true : (oblock instanceof OBlockStairs ? (this.h(i, j, k) & 4) == 4 : (oblock instanceof OBlockHalfSlab ? (this.h(i, j, k) & 8) == 8 : false)));
    }

    public boolean b(int i, int j, int k, boolean flag) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            OChunk ochunk = this.w.d(i >> 4, k >> 4);

            if (ochunk != null && !ochunk.g()) {
                OBlock oblock = OBlock.p[this.a(i, j, k)];

                return oblock == null ? false : oblock.cB.k() && oblock.b();
            } else {
                return flag;
            }
        } else {
            return flag;
        }
    }

    public void x() {
        int i = this.a(1.0F);

        if (i != this.j) {
            this.j = i;
        }
    }

    public void a(boolean flag, boolean flag1) {
        this.E = flag;
        this.F = flag1;
    }

    public void b() {
        this.n();
    }

    private void a() {
        if (this.y.p()) {
            this.n = 1.0F;
            if (this.y.n()) {
                this.p = 1.0F;
            }
        }
    }

    protected void n() {
        if (!this.u.f) {
            int i = this.y.o();

            if (i <= 0) {
                if (this.y.n()) {
                    this.y.f(this.t.nextInt(12000) + 3600);
                } else {
                    this.y.f(this.t.nextInt(168000) + 12000);
                }
            } else {
                --i;
                this.y.f(i);
                if (i <= 0) {
                    // CanaryMod: Thunder hook
                    if (!(Boolean) etc.getLoader().callHook(PluginLoader.Hook.THUNDER_CHANGE, this.world, !this.y.n())) {
                        this.y.a(!this.y.n());
                    } // CanaryMod: diff visibility      
                }
            }

            int j = this.y.q();

            if (j <= 0) {
                if (this.y.p()) {
                    this.y.g(this.t.nextInt(12000) + 12000);
                } else {
                    this.y.g(this.t.nextInt(168000) + 12000);
                }
            } else {
                --j;
                this.y.g(j);
                if (j <= 0) {
                    // CanaryMod: Weather hook
                    if (!(Boolean) etc.getLoader().callHook(PluginLoader.Hook.WEATHER_CHANGE, this.world, !this.y.p())) {
                        this.y.b(!this.y.p());
                    } // CanaryMod: diff visibility
                }
            }

            this.m = this.n;
            if (this.y.p()) {
                this.n = (float) ((double) this.n + 0.01D);
            } else {
                this.n = (float) ((double) this.n - 0.01D);
            }

            if (this.n < 0.0F) {
                this.n = 0.0F;
            }

            if (this.n > 1.0F) {
                this.n = 1.0F;
            }

            this.o = this.p;
            if (this.y.n()) {
                this.p = (float) ((double) this.p + 0.01D);
            } else {
                this.p = (float) ((double) this.p - 0.01D);
            }

            if (this.p < 0.0F) {
                this.p = 0.0F;
            }

            if (this.p > 1.0F) {
                this.p = 1.0F;
            }
        }
    }

    public void y() {
        this.y.g(1);
    }

    protected void z() {
        this.G.clear();
        this.D.a("buildList");

        int i;
        OEntityPlayer oentityplayer;
        int j;
        int k;

        for (i = 0; i < this.h.size(); ++i) {
            oentityplayer = (OEntityPlayer) this.h.get(i);
            j = OMathHelper.c(oentityplayer.t / 16.0D);
            k = OMathHelper.c(oentityplayer.v / 16.0D);
            byte b0 = 7;

            for (int l = -b0; l <= b0; ++l) {
                for (int i1 = -b0; i1 <= b0; ++i1) {
                    this.G.add(new OChunkCoordIntPair(l + j, i1 + k));
                }
            }
        }

        this.D.b();
        if (this.N > 0) {
            --this.N;
        }

        this.D.a("playerCheckLight");
        if (!this.h.isEmpty()) {
            i = this.t.nextInt(this.h.size());
            oentityplayer = (OEntityPlayer) this.h.get(i);
            j = OMathHelper.c(oentityplayer.t) + this.t.nextInt(11) - 5;
            k = OMathHelper.c(oentityplayer.u) + this.t.nextInt(11) - 5;
            int j1 = OMathHelper.c(oentityplayer.v) + this.t.nextInt(11) - 5;

            this.z(j, k, j1);
        }

        this.D.b();
    }

    protected void a(int i, int j, OChunk ochunk) {
        this.D.c("moodSound");
        if (this.N == 0 && !this.I) {
            this.k = this.k * 3 + 1013904223;
            int k = this.k >> 2;
            int l = k & 15;
            int i1 = k >> 8 & 15;
            int j1 = k >> 16 & 127;
            int k1 = ochunk.a(l, j1, i1);

            l += i;
            i1 += j;
            if (k1 == 0 && this.l(l, j1, i1) <= this.t.nextInt(8) && this.b(OEnumSkyBlock.a, l, j1, i1) <= 0) {
                OEntityPlayer oentityplayer = this.a((double) l + 0.5D, (double) j1 + 0.5D, (double) i1 + 0.5D, 8.0D);

                if (oentityplayer != null && oentityplayer.e((double) l + 0.5D, (double) j1 + 0.5D, (double) i1 + 0.5D) > 4.0D) {
                    this.a((double) l + 0.5D, (double) j1 + 0.5D, (double) i1 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.t.nextFloat() * 0.2F);
                    this.N = this.t.nextInt(12000) + 6000;
                }
            }
        }

        this.D.c("checkLight");
        ochunk.o();
    }

    protected void g() {
        this.z();
    }

    public boolean w(int i, int j, int k) {
        return this.c(i, j, k, false);
    }

    public boolean x(int i, int j, int k) {
        return this.c(i, j, k, true);
    }

    public boolean c(int i, int j, int k, boolean flag) {
        OBiomeGenBase obiomegenbase = this.a(i, k);
        float f = obiomegenbase.j();

        if (f > 0.15F) {
            return false;
        } else {
            if (j >= 0 && j < 256 && this.b(OEnumSkyBlock.b, i, j, k) < 10) {
                int l = this.a(i, j, k);

                if ((l == OBlock.E.cm || l == OBlock.D.cm) && this.h(i, j, k) == 0) {
                    if (!flag) {
                        return true;
                    }

                    boolean flag1 = true;

                    if (flag1 && this.g(i - 1, j, k) != OMaterial.h) {
                        flag1 = false;
                    }

                    if (flag1 && this.g(i + 1, j, k) != OMaterial.h) {
                        flag1 = false;
                    }

                    if (flag1 && this.g(i, j, k - 1) != OMaterial.h) {
                        flag1 = false;
                    }

                    if (flag1 && this.g(i, j, k + 1) != OMaterial.h) {
                        flag1 = false;
                    }

                    if (!flag1) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public boolean y(int i, int j, int k) {
        OBiomeGenBase obiomegenbase = this.a(i, k);
        float f = obiomegenbase.j();

        if (f > 0.15F) {
            return false;
        } else {
            if (j >= 0 && j < 256 && this.b(OEnumSkyBlock.b, i, j, k) < 10) {
                int l = this.a(i, j - 1, k);
                int i1 = this.a(i, j, k);

                if (i1 == 0 && OBlock.aV.b(this, i, j, k) && l != 0 && l != OBlock.aW.cm && OBlock.p[l].cB.c()) {
                    return true;
                }
            }

            return false;
        }
    }

    public void z(int i, int j, int k) {
        if (!this.u.f) {
            this.c(OEnumSkyBlock.a, i, j, k);
        }

        this.c(OEnumSkyBlock.b, i, j, k);
    }

    private int b(int i, int j, int k, int l, int i1, int j1) {
        int k1 = 0;

        if (this.k(j, k, l)) {
            k1 = 15;
        } else {
            if (j1 == 0) {
                j1 = 1;
            }

            int l1 = this.b(OEnumSkyBlock.a, j - 1, k, l) - j1;
            int i2 = this.b(OEnumSkyBlock.a, j + 1, k, l) - j1;
            int j2 = this.b(OEnumSkyBlock.a, j, k - 1, l) - j1;
            int k2 = this.b(OEnumSkyBlock.a, j, k + 1, l) - j1;
            int l2 = this.b(OEnumSkyBlock.a, j, k, l - 1) - j1;
            int i3 = this.b(OEnumSkyBlock.a, j, k, l + 1) - j1;

            if (l1 > k1) {
                k1 = l1;
            }

            if (i2 > k1) {
                k1 = i2;
            }

            if (j2 > k1) {
                k1 = j2;
            }

            if (k2 > k1) {
                k1 = k2;
            }

            if (l2 > k1) {
                k1 = l2;
            }

            if (i3 > k1) {
                k1 = i3;
            }
        }

        return k1;
    }

    private int g(int i, int j, int k, int l, int i1, int j1) {
        int k1 = OBlock.t[i1];
        int l1 = this.b(OEnumSkyBlock.b, j - 1, k, l) - j1;
        int i2 = this.b(OEnumSkyBlock.b, j + 1, k, l) - j1;
        int j2 = this.b(OEnumSkyBlock.b, j, k - 1, l) - j1;
        int k2 = this.b(OEnumSkyBlock.b, j, k + 1, l) - j1;
        int l2 = this.b(OEnumSkyBlock.b, j, k, l - 1) - j1;
        int i3 = this.b(OEnumSkyBlock.b, j, k, l + 1) - j1;

        if (l1 > k1) {
            k1 = l1;
        }

        if (i2 > k1) {
            k1 = i2;
        }

        if (j2 > k1) {
            k1 = j2;
        }

        if (k2 > k1) {
            k1 = k2;
        }

        if (l2 > k1) {
            k1 = l2;
        }

        if (i3 > k1) {
            k1 = i3;
        }

        return k1;
    }

    public void c(OEnumSkyBlock oenumskyblock, int i, int j, int k) {
        if (this.a(i, j, k, 17)) {
            int l = 0;
            int i1 = 0;

            this.D.a("getBrightness");
            int j1 = this.b(oenumskyblock, i, j, k);
            boolean flag = false;
            int k1 = this.a(i, j, k);
            int l1 = this.b(i, j, k);

            if (l1 == 0) {
                l1 = 1;
            }

            boolean flag1 = false;
            int i2;

            if (oenumskyblock == OEnumSkyBlock.a) {
                i2 = this.b(j1, i, j, k, k1, l1);
            } else {
                i2 = this.g(j1, i, j, k, k1, l1);
            }

            int j2;
            int k2;
            int l2;
            int i3;
            int j3;
            int k3;
            int l3;
            int i4;

            if (i2 > j1) {
                this.H[i1++] = 133152;
            } else if (i2 < j1) {
                if (oenumskyblock != OEnumSkyBlock.b) {
                    ;
                }

                this.H[i1++] = 133152 + (j1 << 18);

                while (l < i1) {
                    k1 = this.H[l++];
                    l1 = (k1 & 63) - 32 + i;
                    i2 = (k1 >> 6 & 63) - 32 + j;
                    j2 = (k1 >> 12 & 63) - 32 + k;
                    k2 = k1 >> 18 & 15;
                    l2 = this.b(oenumskyblock, l1, i2, j2);
                    if (l2 == k2) {
                        this.b(oenumskyblock, l1, i2, j2, 0);
                        if (k2 > 0) {
                            i3 = l1 - i;
                            k3 = i2 - j;
                            j3 = j2 - k;
                            if (i3 < 0) {
                                i3 = -i3;
                            }

                            if (k3 < 0) {
                                k3 = -k3;
                            }

                            if (j3 < 0) {
                                j3 = -j3;
                            }

                            if (i3 + k3 + j3 < 17) {
                                for (i4 = 0; i4 < 6; ++i4) {
                                    l3 = i4 % 2 * 2 - 1;
                                    int j4 = l1 + i4 / 2 % 3 / 2 * l3;
                                    int k4 = i2 + (i4 / 2 + 1) % 3 / 2 * l3;
                                    int l4 = j2 + (i4 / 2 + 2) % 3 / 2 * l3;

                                    l2 = this.b(oenumskyblock, j4, k4, l4);
                                    int i5 = OBlock.r[this.a(j4, k4, l4)];

                                    if (i5 == 0) {
                                        i5 = 1;
                                    }

                                    if (l2 == k2 - i5 && i1 < this.H.length) {
                                        this.H[i1++] = j4 - i + 32 + (k4 - j + 32 << 6) + (l4 - k + 32 << 12) + (k2 - i5 << 18);
                                    }
                                }
                            }
                        }
                    }
                }

                l = 0;
            }

            this.D.b();
            this.D.a("checkedPosition < toCheckCount");

            while (l < i1) {
                k1 = this.H[l++];
                l1 = (k1 & 63) - 32 + i;
                i2 = (k1 >> 6 & 63) - 32 + j;
                j2 = (k1 >> 12 & 63) - 32 + k;
                k2 = this.b(oenumskyblock, l1, i2, j2);
                l2 = this.a(l1, i2, j2);
                i3 = OBlock.r[l2];
                if (i3 == 0) {
                    i3 = 1;
                }

                boolean flag2 = false;

                if (oenumskyblock == OEnumSkyBlock.a) {
                    k3 = this.b(k2, l1, i2, j2, l2, i3);
                } else {
                    k3 = this.g(k2, l1, i2, j2, l2, i3);
                }

                if (k3 != k2) {
                    this.b(oenumskyblock, l1, i2, j2, k3);
                    if (k3 > k2) {
                        j3 = l1 - i;
                        i4 = i2 - j;
                        l3 = j2 - k;
                        if (j3 < 0) {
                            j3 = -j3;
                        }

                        if (i4 < 0) {
                            i4 = -i4;
                        }

                        if (l3 < 0) {
                            l3 = -l3;
                        }

                        if (j3 + i4 + l3 < 17 && i1 < this.H.length - 6) {
                            if (this.b(oenumskyblock, l1 - 1, i2, j2) < k3) {
                                this.H[i1++] = l1 - 1 - i + 32 + (i2 - j + 32 << 6) + (j2 - k + 32 << 12);
                            }

                            if (this.b(oenumskyblock, l1 + 1, i2, j2) < k3) {
                                this.H[i1++] = l1 + 1 - i + 32 + (i2 - j + 32 << 6) + (j2 - k + 32 << 12);
                            }

                            if (this.b(oenumskyblock, l1, i2 - 1, j2) < k3) {
                                this.H[i1++] = l1 - i + 32 + (i2 - 1 - j + 32 << 6) + (j2 - k + 32 << 12);
                            }

                            if (this.b(oenumskyblock, l1, i2 + 1, j2) < k3) {
                                this.H[i1++] = l1 - i + 32 + (i2 + 1 - j + 32 << 6) + (j2 - k + 32 << 12);
                            }

                            if (this.b(oenumskyblock, l1, i2, j2 - 1) < k3) {
                                this.H[i1++] = l1 - i + 32 + (i2 - j + 32 << 6) + (j2 - 1 - k + 32 << 12);
                            }

                            if (this.b(oenumskyblock, l1, i2, j2 + 1) < k3) {
                                this.H[i1++] = l1 - i + 32 + (i2 - j + 32 << 6) + (j2 + 1 - k + 32 << 12);
                            }
                        }
                    }
                }
            }

            this.D.b();
        }
    }

    public boolean a(boolean flag) {
        return false;
    }

    public List a(OChunk ochunk, boolean flag) {
        return null;
    }

    public List b(OEntity oentity, OAxisAlignedBB oaxisalignedbb) {
        List tempList = new ArrayList(); // CanaryMod: fix ConcurrentModificationException (2 more)
        int i = OMathHelper.c((oaxisalignedbb.a - 2.0D) / 16.0D);
        int j = OMathHelper.c((oaxisalignedbb.d + 2.0D) / 16.0D);
        int k = OMathHelper.c((oaxisalignedbb.c - 2.0D) / 16.0D);
        int l = OMathHelper.c((oaxisalignedbb.f + 2.0D) / 16.0D);

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                if (this.c(i1, j1)) {
                    this.e(i1, j1).a(oentity, oaxisalignedbb, tempList); // CanaryMod

                }
            }
        }

        return tempList; // CanaryMod

    }

    public List a(Class oclass, OAxisAlignedBB oaxisalignedbb) {
        return this.a(oclass, oaxisalignedbb, (OIEntitySelector) null);
    }

    public List a(Class oclass, OAxisAlignedBB oaxisalignedbb, OIEntitySelector oientityselector) {
        int i = OMathHelper.c((oaxisalignedbb.a - 2.0D) / 16.0D);
        int j = OMathHelper.c((oaxisalignedbb.d + 2.0D) / 16.0D);
        int k = OMathHelper.c((oaxisalignedbb.c - 2.0D) / 16.0D);
        int l = OMathHelper.c((oaxisalignedbb.f + 2.0D) / 16.0D);
        ArrayList arraylist = new ArrayList();

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                if (this.c(i1, j1)) {
                    this.e(i1, j1).a(oclass, oaxisalignedbb, arraylist, oientityselector);
                }
            }
        }

        return arraylist;
    }

    public OEntity a(Class oclass, OAxisAlignedBB oaxisalignedbb, OEntity oentity) {
        List list = this.a(oclass, oaxisalignedbb);
        OEntity oentity1 = null;
        double d0 = Double.MAX_VALUE;

        for (int i = 0; i < list.size(); ++i) {
            OEntity oentity2 = (OEntity) list.get(i);

            if (oentity2 != oentity) {
                double d1 = oentity.e(oentity2);

                if (d1 <= d0) {
                    oentity1 = oentity2;
                    d0 = d1;
                }
            }
        }

        return oentity1;
    }

    public abstract OEntity a(int i);

    public void b(int i, int j, int k, OTileEntity otileentity) {
        if (this.f(i, j, k)) {
            this.d(i, k).e();
        }
    }

    public int a(Class oclass) {
        int i = 0;

        for (int j = 0; j < this.e.size(); ++j) {
            OEntity oentity = (OEntity) this.e.get(j);

            if (oclass.isAssignableFrom(oentity.getClass())) {
                ++i;
            }
        }

        return i;
    }

    public void a(List list) {
        this.e.addAll(list);

        for (int i = 0; i < list.size(); ++i) {
            this.a((OEntity) list.get(i));
        }
    }

    public void b(List list) {
        this.f.addAll(list);
    }

    public boolean a(int i, int j, int k, int l, boolean flag, int i1, OEntity oentity) {
        int j1 = this.a(j, k, l);
        OBlock oblock = OBlock.p[j1];
        OBlock oblock1 = OBlock.p[i];
        OAxisAlignedBB oaxisalignedbb = oblock1.e(this, j, k, l);

        if (flag) {
            oaxisalignedbb = null;
        }

        if (oaxisalignedbb != null && !this.a(oaxisalignedbb, oentity)) {
            return false;
        } else {
            if (oblock != null && (oblock == OBlock.D || oblock == OBlock.E || oblock == OBlock.F || oblock == OBlock.G || oblock == OBlock.au || oblock.cB.j())) {
                oblock = null;
            }

            return oblock != null && oblock.cB == OMaterial.q && oblock1 == OBlock.ck ? true : i > 0 && oblock == null && oblock1.b_(this, j, k, l, i1);
        }
    }

    public OPathEntity a(OEntity oentity, OEntity oentity1, float f, boolean flag, boolean flag1, boolean flag2, boolean flag3) {
        this.D.a("pathfind");
        int i = OMathHelper.c(oentity.t);
        int j = OMathHelper.c(oentity.u + 1.0D);
        int k = OMathHelper.c(oentity.v);
        int l = (int) (f + 16.0F);
        int i1 = i - l;
        int j1 = j - l;
        int k1 = k - l;
        int l1 = i + l;
        int i2 = j + l;
        int j2 = k + l;
        OChunkCache ochunkcache = new OChunkCache(this, i1, j1, k1, l1, i2, j2);
        OPathEntity opathentity = (new OPathFinder(ochunkcache, flag, flag1, flag2, flag3)).a(oentity, oentity1, f);

        this.D.b();
        return opathentity;
    }

    public OPathEntity a(OEntity oentity, int i, int j, int k, float f, boolean flag, boolean flag1, boolean flag2, boolean flag3) {
        this.D.a("pathfind");
        int l = OMathHelper.c(oentity.t);
        int i1 = OMathHelper.c(oentity.u);
        int j1 = OMathHelper.c(oentity.v);
        int k1 = (int) (f + 8.0F);
        int l1 = l - k1;
        int i2 = i1 - k1;
        int j2 = j1 - k1;
        int k2 = l + k1;
        int l2 = i1 + k1;
        int i3 = j1 + k1;
        OChunkCache ochunkcache = new OChunkCache(this, l1, i2, j2, k2, l2, i3);
        OPathEntity opathentity = (new OPathFinder(ochunkcache, flag, flag1, flag2, flag3)).a(oentity, i, j, k, f);

        this.D.b();
        return opathentity;
    }

    public boolean k(int i, int j, int k, int l) {
        int i1 = this.a(i, j, k);

        return i1 == 0 ? false : OBlock.p[i1].c((OIBlockAccess) this, i, j, k, l);
    }

    public boolean A(int i, int j, int k) {
        return this.k(i, j - 1, k, 0) ? true : (this.k(i, j + 1, k, 1) ? true : (this.k(i, j, k - 1, 2) ? true : (this.k(i, j, k + 1, 3) ? true : (this.k(i - 1, j, k, 4) ? true : this.k(i + 1, j, k, 5)))));
    }

    public boolean l(int i, int j, int k, int l) {
        if (this.t(i, j, k)) {
            return this.A(i, j, k);
        } else {
            int i1 = this.a(i, j, k);

            return i1 == 0 ? false : OBlock.p[i1].b(this, i, j, k, l);
        }
    }

    public boolean B(int i, int j, int k) {
        return this.l(i, j - 1, k, 0) ? true : (this.l(i, j + 1, k, 1) ? true : (this.l(i, j, k - 1, 2) ? true : (this.l(i, j, k + 1, 3) ? true : (this.l(i - 1, j, k, 4) ? true : this.l(i + 1, j, k, 5)))));
    }

    public OEntityPlayer a(OEntity oentity, double d0) {
        return this.a(oentity.t, oentity.u, oentity.v, d0);
    }

    public OEntityPlayer a(double d0, double d1, double d2, double d3) {
        double d4 = -1.0D;
        OEntityPlayer oentityplayer = null;

        for (int i = 0; i < this.h.size(); ++i) {
            OEntityPlayer oentityplayer1 = (OEntityPlayer) this.h.get(i);
            double d5 = oentityplayer1.e(d0, d1, d2);

            if ((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1.0D || d5 < d4)) {
                d4 = d5;
                oentityplayer = oentityplayer1;
            }
        }

        return oentityplayer;
    }

    public OEntityPlayer b(OEntity oentity, double d0) {
        return this.b(oentity.t, oentity.u, oentity.v, d0);
    }

    public OEntityPlayer b(double d0, double d1, double d2, double d3) {
        double d4 = -1.0D;
        OEntityPlayer oentityplayer = null;

        for (int i = 0; i < this.h.size(); ++i) {
            OEntityPlayer oentityplayer1 = (OEntityPlayer) this.h.get(i);

            if (!oentityplayer1.cd.a && oentityplayer1.S()) {
                double d5 = oentityplayer1.e(d0, d1, d2);
                double d6 = d3;

                if (oentityplayer1.ah()) {
                    d6 = d3 * 0.800000011920929D;
                }

                if (oentityplayer1.aj()) {
                    float f = oentityplayer1.bR();

                    if (f < 0.1F) {
                        f = 0.1F;
                    }

                    d6 *= (double) (0.7F * f);
                }

                if ((d3 < 0.0D || d5 < d6 * d6) && (d4 == -1.0D || d5 < d4)) {
                    d4 = d5;
                    oentityplayer = oentityplayer1;
                }
            }
        }

        return oentityplayer;
    }

    public OEntityPlayer a(String s) {
        for (int i = 0; i < this.h.size(); ++i) {
            if (s.equals(((OEntityPlayer) this.h.get(i)).bR)) {
                return (OEntityPlayer) this.h.get(i);
            }
        }

        return null;
    }

    public void D() {
        this.x.c();
    }

    public long E() {
        return this.y.b();
    }

    public long F() {
        return this.y.f();
    }

    public long G() {
        return this.y.g();
    }

    public void b(long i) {
        if (!(Boolean) etc.getLoader().callHook(PluginLoader.Hook.TIME_CHANGE, this.world, i)) {
            this.y.c(i);
        } // CanaryMod: diff visibility
    }

    public OChunkCoordinates H() {
        return new OChunkCoordinates(this.y.c(), this.y.d(), this.y.e());
    }

    public boolean a(OEntityPlayer oentityplayer, int i, int j, int k) {
        return true;
    }

    public void a(OEntity oentity, byte b0) {}

    public OIChunkProvider I() {
        return this.w;
    }

    public void c(int i, int j, int k, int l, int i1, int j1) {
        if (l > 0) {
            OBlock.p[l].b(this, i, j, k, i1, j1);
        }
    }

    public OISaveHandler J() {
        return this.x;
    }

    public OWorldInfo K() {
        return this.y;
    }

    public OGameRules L() {
        return this.y.x();
    }

    public void c() {}

    public float i(float f) {
        return (this.o + (this.p - this.o) * f) * this.j(f);
    }

    public float j(float f) {
        return this.m + (this.n - this.m) * f;
    }

    public boolean M() {
        return (double) this.i(1.0F) > 0.9D;
    }

    public boolean N() {
        return (double) this.j(1.0F) > 0.2D;
    }

    public boolean D(int i, int j, int k) {
        if (!this.N()) {
            return false;
        } else if (!this.k(i, j, k)) {
            return false;
        } else if (this.h(i, k) > j) {
            return false;
        } else {
            OBiomeGenBase obiomegenbase = this.a(i, k);

            return obiomegenbase.c() ? false : obiomegenbase.d();
        }
    }

    public boolean E(int i, int j, int k) {
        OBiomeGenBase obiomegenbase = this.a(i, k);

        return obiomegenbase.e();
    }

    public void a(String s, OWorldSavedData oworldsaveddata) {
        this.A.a(s, oworldsaveddata);
    }

    public OWorldSavedData a(Class oclass, String s) {
        return this.A.a(oclass, s);
    }

    public int b(String s) {
        return this.A.a(s);
    }

    public void e(int i, int j, int k, int l, int i1) {
        for (int j1 = 0; j1 < this.v.size(); ++j1) {
            ((OIWorldAccess) this.v.get(j1)).a(i, j, k, l, i1);
        }
    }

    public void f(int i, int j, int k, int l, int i1) {
        this.a((OEntityPlayer) null, i, j, k, l, i1);
    }

    public void a(OEntityPlayer oentityplayer, int i, int j, int k, int l, int i1) {
        try {
            for (int j1 = 0; j1 < this.v.size(); ++j1) {
                ((OIWorldAccess) this.v.get(j1)).a(oentityplayer, i, j, k, l, i1);
            }
        } catch (Throwable throwable) {
            OCrashReport ocrashreport = OCrashReport.a(throwable, "Playing level event");
            OCrashReportCategory ocrashreportcategory = ocrashreport.a("Level event being played");

            ocrashreportcategory.a("Block coordinates", OCrashReportCategory.a(j, k, l));
            ocrashreportcategory.a("Event source", oentityplayer);
            ocrashreportcategory.a("Event type", Integer.valueOf(i));
            ocrashreportcategory.a("Event data", Integer.valueOf(i1));
            throw new OReportedException(ocrashreport);
        }
    }

    public int O() {
        return 256;
    }

    public int P() {
        return this.u.f ? 128 : 256;
    }

    public OIUpdatePlayerListBox a(OEntityMinecart oentityminecart) {
        return null;
    }

    public Random F(int i, int j, int k) {
        long l = (long) i * 341873128712L + (long) j * 132897987541L + this.K().b() + (long) k;

        this.t.setSeed(l);
        return this.t;
    }

    public OChunkPosition b(String s, int i, int j, int k) {
        return this.I().a(this, s, i, j, k);
    }

    public OCrashReportCategory a(OCrashReport ocrashreport) {
        OCrashReportCategory ocrashreportcategory = ocrashreport.a("Affected level", 1);

        ocrashreportcategory.a("Level name", (this.y == null ? "????" : this.y.k()));
        ocrashreportcategory.a("All players", (Callable) (new OCallableLvl2(this)));
        ocrashreportcategory.a("Chunk stats", (Callable) (new OCallableLvl3(this)));

        try {
            this.y.a(ocrashreportcategory);
        } catch (Throwable throwable) {
            ocrashreportcategory.a("Level Data Unobtainable", throwable);
        }

        return ocrashreportcategory;
    }

    public void g(int i, int j, int k, int l, int i1) {
        for (int j1 = 0; j1 < this.v.size(); ++j1) {
            OIWorldAccess oiworldaccess = (OIWorldAccess) this.v.get(j1);

            oiworldaccess.b(i, j, k, l, i1);
        }
    }

    public OVec3Pool S() {
        return this.J;
    }

    public Calendar T() {
        if (this.F() % 600L == 0L) {
            this.K.setTimeInMillis(System.currentTimeMillis());
        }

        return this.K;
    }
}
