package business;

/**
 * The type of events
 */
public enum EventType {

    TETEATETE(6, true), BANDOSENTADO(1000, true), MULTIDAOEMPE(500000, false);

    private int maxAssistanceDimension;
    private boolean isSeated;

    private EventType(int maxAssistanceDimension, boolean isSeated) {
        this.maxAssistanceDimension = maxAssistanceDimension;
        this.isSeated = isSeated;
    }

    /**
     * Retorna assistencia max do tipo de evento
     * @return assistencia max
     */
    public int getAssistance() {
        return this.maxAssistanceDimension;
    }
    
    /**
     * Retorna se tipo de evento e sentado ou nao
     * @return true se e sentado, false se nao
     */
    public boolean isSeated() {
        return this.isSeated;
    }


}