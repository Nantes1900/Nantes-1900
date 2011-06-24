package CodeFinal;

public class Tuilage {

	protected Case[][][] _quad;

	protected double _minX, _maxX, _minY, _maxY, _minZ, _maxZ;
	protected int _nbX, _nbY, _nbZ;
	
	public Tuilage(double minX, double maxX, double minY, double maxY, double minZ, double maxZ, 
							int nbX, int nbY, int nbZ) {
		_minX = minX;		_maxX = maxX;
		_minY = minY;		_maxY = maxY;
		_minZ = minZ;		_maxZ = maxZ;
		_nbX = nbX;			_nbY = nbY;			_nbZ = nbZ;
		
		_quad = new Case[_nbX][_nbY][_nbZ];
		for(int i = 0; i < _nbX; i ++) {
			for(int j = 0; j < _nbY; j ++) {
				for(int k = 0; k < _nbZ; k ++) {
					_quad[i][j][k] = new Case();
				}
			}
		}
	}
	
	public Tuilage(EnsembleFaces e, int nbX, int nbY, int nbZ) {
		this(e.xMin(), e.xMax(), e.yMin(), e.yMax(), e.zMin(), e.zMax(), nbX, nbY, nbZ);
		this.addEnsembleFaces(e);
	}
	
	public void clear() {
		for(int i = 0; i < _nbX; i ++) {
			for(int j = 0; j < _nbY; j ++) {
				for(int k = 0; k < _nbZ; k ++) {
					_quad[i][j][k].clear();
				}
			}
		}
	}
	
	public void addEnsembleFaces(EnsembleFaces e) {
		for(Triangle f : e) {
			addFace(f);
		}
	}
	
	//TODO : A tester !
	public void addFace(Triangle f) {
		addFace(f, caseX(f.p0._x), caseY(f.p0._y), caseZ(f.p0._z));
		addFace(f, caseX(f.p1._x), caseY(f.p1._y), caseZ(f.p1._z));
		addFace(f, caseX(f.p2._x), caseY(f.p2._y), caseZ(f.p2._z));
	}
	
	//TODO : A TESTER ! ENLEVER LES DIVISIONS ET LES FAIRE DANS LE CONSTRUCTEUR !
	public int caseX(double x) {
		return (int)Math.abs((x - _minX) * (double)_nbX / (_maxX + 1 - _minX));
	}
	
	public int caseY(double y) {
		return (int)Math.abs((y - _minY) * (double)_nbY / (_maxY + 1 - _minY));
	}
	
	public int caseZ(double z) {
		return (int)Math.abs((z - _minZ) * (double)_nbZ / (_maxZ + 1 - _minZ));
	}
	
	public void addFace(Triangle f, int x, int y, int z) {
		_quad[x][y][z].addFace(f);
	}
	
	public void findNeighbours() {
		for(int i = 0; i < _nbX; i ++) {
			for(int j = 0; j < _nbY; j ++) {
				for(int k = 0; k < _nbZ; k ++) {
					_quad[i][j][k].findNeighbours();
				}
			}
		}
	}
}
