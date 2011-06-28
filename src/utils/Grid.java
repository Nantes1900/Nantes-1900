package utils;

import modeles.Mesh;
import modeles.Triangle;

public class Grid {

	private Case[][][] quad;

	private double _minX, _maxX, _minY, _maxY, _minZ, _maxZ;
	private int _nbX, _nbY, _nbZ;

	public Grid(double minX, double maxX, double minY, double maxY, double minZ, double maxZ, 
							int nbX, int nbY, int nbZ) {
		_minX = minX;		_maxX = maxX;
		_minY = minY;		_maxY = maxY;
		_minZ = minZ;		_maxZ = maxZ;
		_nbX = nbX;			_nbY = nbY;			_nbZ = nbZ;
		
		quad = new Case[_nbX][_nbY][_nbZ];
		for(int i = 0; i < _nbX; i ++) {
			for(int j = 0; j < _nbY; j ++) {
				for(int k = 0; k < _nbZ; k ++) {
					quad[i][j][k] = new Case();
				}
			}
		}
	}
	
	public Case[][][] getQuad() {
		return quad;
	}

	public void setQuad(Case[][][] quad) {
		this.quad = quad;
	}
	
	public Grid(Mesh e, int nbX, int nbY, int nbZ) {
		this(e.xMin(), e.xMax(), e.yMin(), e.yMax(), e.zMin(), e.zMax(), nbX, nbY, nbZ);
		this.addEnsembleFaces(e);
	}
	
	public void clear() {
		for(int i = 0; i < _nbX; i ++) {
			for(int j = 0; j < _nbY; j ++) {
				for(int k = 0; k < _nbZ; k ++) {
					quad[i][j][k].clear();
				}
			}
		}
	}
	
	public void addEnsembleFaces(Mesh e) {
		for(Triangle f : e) {
			addFace(f);
		}
	}
	
	//TODO : A tester !
	public void addFace(Triangle f) {
		addFace(f, caseX(f.getP0().getX()), caseY(f.getP0().getY()), caseZ(f.getP0().getZ()));
		addFace(f, caseX(f.getP1().getX()), caseY(f.getP1().getY()), caseZ(f.getP1().getZ()));
		addFace(f, caseX(f.getP2().getX()), caseY(f.getP2().getY()), caseZ(f.getP2().getZ()));
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
		quad[x][y][z].addFace(f);
	}
	
	public void findNeighbours() {
		System.out.println("Searching for neighbours...");
		for(int i = 0; i < _nbX; i ++) {
			for(int j = 0; j < _nbY; j ++) {
				for(int k = 0; k < _nbZ; k ++) {
					quad[i][j][k].findNeighbours();
				}
			}
		}
		System.out.println("Searching finished !");
	}
}
