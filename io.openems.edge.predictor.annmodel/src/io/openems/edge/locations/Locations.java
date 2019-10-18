package io.openems.edge.locations;

/**
 * @author Jan Seidemann
 */
public enum Locations {
    FEMS333(null, null),
    FEMS442(48.365990, 10.893040),
    FEMS450(49.123020, 12.127700),
    FEMS474(48.549100, 11.867990),
    FEMS487(49.597150, 11.076050),
    FEMS488(48.105530, 14.574810),
    FEMS494(47.117350, 7.300530),
    FEMS503(47.309510, 7.765900),
    FEMS508(47.069730, 15.438260),
    FEMS999(50.166400, 9.787010),
    FEMS820(48.775845, 9.182932);

    private final Double latitude;
    private final Double longitude;

    Locations(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }
}
