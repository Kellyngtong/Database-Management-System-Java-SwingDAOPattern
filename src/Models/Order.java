package Models;

public class Order {
    private int id;
    private int userId;
    private int productId;
    private int cuantity;
    private String date;

    public Order(int userId, int productId, int cuantity, String date) {
        this.userId = userId;
        this.productId = productId;
        this.cuantity = cuantity;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCuantity() {
        return cuantity;
    }

    public void setCuantity(int cuantity) {
        this.cuantity = cuantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", cuantity=" + cuantity +
                ", date='" + date + '\'' +
                '}';
    }

}