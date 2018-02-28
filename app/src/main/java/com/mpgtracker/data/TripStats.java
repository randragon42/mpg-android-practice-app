package com.mpgtracker.data;


import java.util.List;

public class TripStats {

    private double mAverageCost;
    private double mTotalCost;
    private double mHighestCost;
    private double mLowestCost;

    private double mAverageMiles;
    private double mTotalMiles;
    private double mMostMiles;
    private double mLeastMiles;

    private double mAverageGallons;
    private double mTotalGallons;
    private double mMostGallons;
    private double mLeastGallons;

    private double mAverageMpg;
    private double mBestMpg;
    private double mWorstMpg;

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

    public double getAverageMiles() {
        return mAverageMiles;
    }

    public double getTotalMiles() {
        return mTotalMiles;
    }

    public double getMostMiles() {
        return mMostMiles;
    }

    public double getLeastMiles() {
        return mLeastMiles;
    }

    public double getAverageGallons() {
        return mAverageGallons;
    }

    public double getTotalGallons() {
        return mTotalGallons;
    }

    public double getMostGallons() {
        return mMostGallons;
    }

    public double getLeastGallons() {
        return mLeastGallons;
    }

    public double getAverageMpg() {
        return mAverageMpg;
    }

    public double getBestMpg() {
        return mBestMpg;
    }

    public double getWorstMpg() {
        return mWorstMpg;
    }

    TripStats(List<Trip> trips) {
        calculateTripStats(trips);
    }

    void calculateTripStats(List<Trip> trips) {
        double totalCost = 0;
        double highestCost = 0;
        double lowestCost = Double.MAX_VALUE;

        double totalMiles = 0;
        double mostMiles = 0;
        double leastMiles = Double.MAX_VALUE;

        double totalGallons = 0;
        double mostGallons = 0;
        double leastGallons = Double.MAX_VALUE;

        double bestMpg = 0;
        double totalMpg = 0;
        double worstMpg = Double.MAX_VALUE;

        for(int i=0; i<trips.size(); i++){
            Trip trip = trips.get(i);
            totalCost = totalCost + trip.getTripCost();
            totalMiles = totalMiles + trip.getMiles();
            totalGallons = totalGallons + trip.getGallons();

            highestCost = highestCost < trip.getTripCost() ? trip.getTripCost() : highestCost;
            mostMiles = mostMiles < trip.getMiles() ? trip.getMiles() : mostMiles;
            mostGallons = mostGallons < trip.getGallons() ? trip.getGallons() : mostGallons;

            lowestCost = lowestCost > trip.getTripCost() ? trip.getTripCost() : lowestCost;
            leastMiles = leastMiles > trip.getMiles() ? trip.getMiles() : leastMiles;
            leastGallons = leastGallons > trip.getGallons() ? trip.getGallons() : leastGallons;

            if(trip.filledTank){
                totalMpg += trip.getMilesPerGallon();
                worstMpg = worstMpg > trip.getMilesPerGallon() ? trip.getMilesPerGallon() : worstMpg;
                bestMpg = bestMpg < trip.getMilesPerGallon() ? trip.getMilesPerGallon() : bestMpg;
            }
        }

        mAverageCost = totalCost/trips.size();
        mTotalCost = totalCost;
        mHighestCost = highestCost;
        mLowestCost = lowestCost;
        mAverageMiles = totalMiles/trips.size();
        mTotalMiles = totalMiles;
        mMostMiles = mostMiles;
        mLeastMiles = leastMiles;
        mAverageGallons = totalGallons/trips.size();
        mTotalGallons = totalGallons;
        mMostGallons = mostGallons;
        mLeastGallons = leastGallons;
        mAverageMpg = totalMpg/trips.size();
        mBestMpg = bestMpg;
        mWorstMpg = worstMpg;
    }
}
