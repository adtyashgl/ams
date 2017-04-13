package adiractech.ams.listelements;

/**
 * Created by root on 13/04/17.
 */

public class FranchiseElement {
    private int franchiseId;
    private String name;
    private String address;

    public FranchiseElement(String name, String address, int id) {
        this.name = name;
        this.address = address;
        this.franchiseId = id;
    }

    public int getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(int franchiseId) {
        this.franchiseId = franchiseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
