package entity;

public class Provider {
    private int providerID, contactID;
    private String name, shortName, address, description, phone, mail, fax, status, bankAccount;
    private boolean active;
    private int liquidate, totalMoney;
    //liquidate - Nợ phải trả


    public Provider(int providerID, int contactID, String name, String shortName, String address, String description,
                    String phone, String mail, String fax, boolean goodProvider, boolean badProvider, boolean active,
                    int liquidate, int totalMoney, String bankAccount) {
        this.providerID = providerID;
        this.contactID = contactID;
        this.name = name;
        this.shortName = shortName;
        this.address = address;
        this.description = description;
        this.phone = phone;
        this.mail = mail;
        this.fax = fax;
        if (goodProvider) {
            this.status = "Tốt";
        }
        if (badProvider) {
            this.status = "Kém";
        }
        if (!goodProvider && !badProvider) {
            this.status = "Chưa đánh giá";
        }
        this.active = active;
        this.liquidate = liquidate;
        this.totalMoney = totalMoney;
        this.bankAccount = bankAccount;
    }

    public int getProviderID() {
        return providerID;
    }

    public int getContactID() {
        return contactID;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public String getFax() {
        return fax;
    }

    public String getStatus() {
        return status;
    }

    public boolean isActive() {
        return active;
    }

    public int getLiquidate() {
        return liquidate;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    @Override
    public String toString() {
        return this.shortName + "-" + this.name;
    }
}
