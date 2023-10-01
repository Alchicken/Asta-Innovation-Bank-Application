
package admin;

public class InvestorsInfo {
    
    private String name;
    private String icn;
    private String mobileno;
    private String address;
    private String acn;
    private String pin;
    private String acctype;
    private String balance;

    public InvestorsInfo(String name, String icn, String mobileno, String address, String acn, String pin, String acctype, String balance) {
        this.name = name;
        this.icn = icn;
        this.mobileno = mobileno;
        this.address = address;
        this.acn = acn;
        this.pin = pin;
        this.acctype = acctype;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public String getIcn() {
        return icn;
    }

    public String getMobileno() {
        return mobileno;
    }

    public String getAddress() {
        return address;
    }

    public String getAcn() {
        return acn;
    }

    public String getPin() {
        return pin;
    }

    public String getAcctype() {
        return acctype;
    }

    public String getBalance() {
        return balance;
    }
    
    
}
