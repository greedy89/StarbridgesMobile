package id.co.indocyber.android.starbridges.model.TransportReimbursementType;

public class TransportReimbursementType {
    private String name;
    private String reimbursementTranpsportTypeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReimbursementTranpsportTypeId() {
        return reimbursementTranpsportTypeId;
    }

    public void setReimbursementTranpsportTypeId(String reimbursementTranpsportTypeId) {
        this.reimbursementTranpsportTypeId = reimbursementTranpsportTypeId;
    }

    @Override
    public String toString() {
        return name;
    }
}
