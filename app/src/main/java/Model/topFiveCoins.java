package Model;

/**
 * Created by GeneralKolo on 2018/03/13.
 */

public class topFiveCoins {
    private String name;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String symbol;

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    private String price;

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String change1hr;

    public String getChange1hr() {
        return this.change1hr;
    }

    public void setChange1hr(String change1hr) {
        this.change1hr = change1hr;
    }

    private String change24h;

    public String getChange24h() {
        return this.change24h;
    }

    public void setChange24h(String change24h) {
        this.change24h = change24h;
    }

    private String change7d;

    public String getChange7d() {
        return this.change7d;
    }

    public void setChange7d(String change7d) {
        this.change7d = change7d;
    }
}
