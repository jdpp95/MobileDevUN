package co.edu.unal.jdporrasp.devcompanyfinder.model;

public class Company {
    private long id;
    private String name;
    private String website;
    private String phoneNumber;
    private String email;
    private String productsAndServices;
    private String category;

    public Company(long id, String name, String website, String phoneNumber, String email, String productsAndServices, String category) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.productsAndServices = productsAndServices;
        this.category = category;
    }

    public Company() {

    }

    @Override
    public String toString() {
        return this.getId() + ". " + this.getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProductsAndServices() {
        return productsAndServices;
    }

    public void setProductsAndServices(String productsAndServices) {
        this.productsAndServices = productsAndServices;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
