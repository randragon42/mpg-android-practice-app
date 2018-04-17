package com.mpgtracker.data.trips;


import java.util.List;

public class TripStats {

    private double mAverageCost;
    private double mTotalCost;
    private double mHighestCost;
    private double mLowestCost;

    private double mAverageDistance;
    private double mTotalDistance;
    private double mMostDistance;
    private double mLeastDistance;

    private double mAverageVolume;
    private double mTotalVolume;
    private double mMostVolume;
    private double mLeastVolume;

    private double mAverageEfficiency;
    private double mBestEfficiency;
    private double mWorstEfficiency;

    public double getAverageCost() {
        return mAverageCost;
    }

    public double getTotalCost() {
        return mTotalCost;
    }

    public double getHighestCost() {
        return mHighestCost;
    }

    public double getLowestCost() {
        return mLowestCost;
    }

    public double getAverageDistance() {
        return mAverageDistance;
    }

    public double getTotalDistance() {
        return mTotalDistance;
    }

    public double getMostDistance() {
        return mMostDistance;
    }

    public double getLeastDistance() {
        return mLeastDistance;
    }

    public double getAverageVolume() {
        return mAverageVolume;
    }

    public double getTotalVolume() {
        return mTotalVolume;
    }

    public double getMostVolume() {
        return mMostVolume;
    }

    public double getLeastVolume() {
        return mLeastVolume;
    }

    public double getAverageEfficiency() {
        return mAverageEfficiency;
    }

    public double getBestEfficiency() {
        return mBestEfficiency;
    }

    public double getWorstEfficiency() {
        return mWorstEfficiency;
    }

    public TripStats(List<Trip> trips) {
        calculateTripStats(trips);
    }

    void calculateTripStats(List<Trip> trips) {
        double totalCost = 0;
        double highestCost = 0;
        double lowestCost = Double.MAX_VALUE;

        double totalDistance = 0;
        double mostDistance = 0;
        double leastDistance = Double.MAX_VALUE;

        double totalVolume = 0;
        double mostVolume = 0;
        double leastVolume = Double.MAX_VALUE;

        double bestEfficiency = 0;
        double totalEfficiency = 0;
        double worstEfficiency = Double.MAX_VALUE;

        for(int i=0; i<trips.size(); i++){
            Trip trip = trips.get(i);
            totalCost = totalCost + trip.getTripCost();
            totalDistance = totalDistance + trip.getDistance();
            totalVolume = totalVolume + trip.getVolume();

            highestCost = highestCost < trip.getTripCost() ? trip.getTripCost() : highestCost;
            mostDistance = mostDistance < trip.getDistance() ? trip.getDistance() : mostDistance;
            mostVolume = mostVolume < trip.getVolume() ? trip.getVolume() : mostVolume;

            lowestCost = lowestCost > trip.getTripCost() ? trip.getTripCost() : lowestCost;
            leastDistance = leastDistance > trip.getDistance() ? trip.getDistance() : leastDistance;
            leastVolume = leastVolume > trip.getVolume() ? trip.getVolume() : leastVolume;

            if(trip.filledTank){
                totalEfficiency += trip.getEfficiency();
                worstEfficiency = worstEfficiency > trip.getEfficiency() ? trip.getEfficiency() : worstEfficiency;
                bestEfficiency = bestEfficiency < trip.getEfficiency() ? trip.getEfficiency() : bestEfficiency;
            }
        }

        mAverageCost = totalCost/trips.size();
        mTotalCost = totalCost;
        mHighestCost = highestCost;
        mLowestCost = lowestCost;
        mAverageDistance = totalDistance/trips.size();
        mTotalDistance = totalDistance;
        mMostDistance = mostDistance;
        mLeastDistance = leastDistance;
        mAverageVolume = totalVolume/trips.size();
        mTotalVolume = totalVolume;
        mMostVolume = mostVolume;
        mLeastVolume = leastVolume;
        mAverageEfficiency = totalEfficiency/trips.size();
        mBestEfficiency = bestEfficiency;
        mWorstEfficiency = worstEfficiency;
    }
}
