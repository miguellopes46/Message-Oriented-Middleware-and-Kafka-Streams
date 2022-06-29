package data;

import java.io.Serializable;

public class Currency implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private double exchange_rate;

    public Currency(String name, double exchange_rate) {
        this.name = name;
        this.exchange_rate = exchange_rate;
    }

    public Currency() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(double exchange_rate) {
        this.exchange_rate = exchange_rate;
    }
}
