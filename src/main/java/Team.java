public class Team {

    private String name;
    private String federation;
    private int pot;

    public Team(String name, String federation, int pot) {
        this.name = name;
        this.federation = federation;
        this.pot = pot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFederation() {
        return federation;
    }

    public void setFederation(String federation) {
        this.federation = federation;
    }

    public int getPot() {
        return pot;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }

}
