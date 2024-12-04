package vedruna.webClient.DTO;

public class ProductoDTO {


    public String title;
    public String price;

    public ProductoDTO(){

    }

    public ProductoDTO(String nombre, String precio){
        this.title = nombre;
        this.price = precio;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
