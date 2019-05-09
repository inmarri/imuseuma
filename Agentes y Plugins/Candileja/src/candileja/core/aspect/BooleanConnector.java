/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package candileja.core.aspect;

// Por ahora solo se usan los NOT

/**
 *
 * @author Inma
 */
public class BooleanConnector {

    public static final int NOT=0;
    public static final int OR=1;
    public static final int AND=2;
    public static final int XOR=3;

    private static final String[] booleanConnectors = new String[3];
	static { // initialization of the Vector of performatives
		booleanConnectors[NOT]="NOT";
		booleanConnectors[OR]="OR";
		booleanConnectors[AND]="AND";
                booleanConnectors[XOR]="XOR";
	}


    private int booleanConnector;

    public BooleanConnector(){
        this.booleanConnector=0;
    }

    public BooleanConnector(int sc){
        this.booleanConnector = sc;
    }

    public String getBooleanConnector() {
        return booleanConnectors[booleanConnector];
    }

    public void setBooleanConnector(int sc) {
        this.booleanConnector = sc;
    }

    public boolean equals(int sc){
        return booleanConnector==sc;
    }

    @SuppressWarnings("static-access")
	public boolean equals(String sc){
        return this.booleanConnectors[booleanConnector].equals(sc);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + booleanConnector;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BooleanConnector other = (BooleanConnector) obj;
		if (booleanConnector != other.booleanConnector)
			return false;
		return true;
	}

    
}
