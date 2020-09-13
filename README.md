# vCare

vCare helps you detect early stage lung cancer with data from your smarwatches and fitness trackers using Machine Learning.
Made by @abishekvashok, @pranavgade20 during PennApps Hackathon
## Inspiration and back story
Every 2 minute a person in the United States dies due to Lung Cancer. And diagnosis often come out as positive only at last stages where treatment and care becomes difficult. The race for survival then starts. 1.7M deaths are reported throughout the world every years due to Lung Cancer reports World Health Organisation. Such a large magnitude of people lose their loved ones, imagine how hard it can be when you are one among them.

WHO highlights that early stage detection of Lung Cancer can help to a large extend but it is something that is not happening. So we went forward a Neural Network that predicted one's probability of having lung cancer with data from wearable devices such as smart watches and fitness trackers.

## What it is:
vCare is an Android App that uses Google Fit API to get heartbeat data from a wide range of wearable devices such as Apple Watch, Fitbit watches, MI Bands etc. It conveys the heartbeat data along with static parameters such as age and gender to the machine learning model that is deployed on the Cloud.

The Machine Learning Model predicts with 95% accuracy whether a person has Early Stage Lung Cancer. The app fetches this information and if a positive case is found alerts the user to do a routine checkup so that Lung Caner can easily be identified at an early stage and a life can be saved :)

## How we built it
We used data available from Physionet Mimic III clinical database and since it was available pre-loaded on big Query with Google Cloud, we were able to fetch the information we needed and find out whether such a project was feasible. We found out it was, trained a machine learning model on Google Cloud and deployed it. We then found out that we exhausted all the promotional credits ($25) in the process, so had to resort to another vpn one of us owned.

The Android app checks for heartbeat activity and conveys to the model everyday at a random time with the help of a background service. The data collected has no identifiers and is not stored on any ends except on the app. The android app is written in Java.

## Challenges we ran into
At first, we tried to use just the Fitbit API but the API was not providing us with the correct details. So we had to switch to Google Fit, even though Google Fit had pretty much poor documentation related to heart beat activity, we were able to achieve a working model after lots of experimentation. We had to learn End-to-End deployment on Google Cloud, which however seemed difficult at first, was in fact easy and cool.

At the last moment, we discovered rising bills and had to delete the model off Google Cloud because our promotional credits where already exhausted. We got the model from our bucket and hosted it on one of the VPNs we have.

## What we learned
We learned to harness the Google Fit API, deploy machine learning models end to end on Google Cloud, train models in Google Cloud and became accustomed to BigQuery!

## What's next for vCare
Although Google Fit Integrates nicely with a wide range of wearable devices, we would like to extend the reach of the project outside the domain covered by Google Fit. We would also like to present iOS users with a native app, vCare is yet to be launched on the play sotre, so this is something, we are looking forward to.
