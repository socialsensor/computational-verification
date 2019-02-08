computational-verification
==========================

A framework for "learning" how to classify social content as truthful/reliable or not.

Getting started
---------------
#####Part I#####
This part contains information on how to use the framework for:
* Classifying tweets using the models trained on our data.
* Building models and classifying additional data. 
* Implementing cross-validation to additional data.

#####Part II#####
Additionally, the following functionalities that constitute part of the above process are presented:
* Extracting Item(Content) features from a tweet.
* Extracting User features from the tweet's user.
* Extracting Total features (Item & User features merged).

#####Part III#####
This part presents how to use the framework for classifying items using the agreement based retraining technique.

Part I
------------

###Classifying tweets using the models trained on our data###

Each tweet corresponds to a `MediaItem` object. In order to store and access the tweet(s), we currently support [MongoDB](http://www.mongodb.org/) storage. Here are the steps for classifying a single tweet or a list of them:

1. Get access to the mongodb where the MediaItems are stored.

  `MediaItemDAOImpl dao = new MediaItemDAOImpl("ip", "dbname", "collectionname");`
2. Obtain the MediaItem `MediaItem item = dao.getMediaItem("tweetid");` 

   or the list of MediaItems `List<MediaItem> listMedia = dao.getLastMediaItems(numTweets);`.
3. Define ImageVerificationResult object(s)
 
   `ImageVerificationResult verif = new ImageVerificationResult();`  for a Media Item or

   `List<ImageVerificationResult> verifs = new ArrayList<ImageVerificationResult>();` for a list of MediaItems.
   
   to obtain the verification result(s).
4. Obtain the verification result(s) for the Item features extraction by calling

   `verif = tweetClassificationTotalMedia(item);` for a MediaItem or
   
   `verif = tweetClassificationTotalMedia(listMedia);` for a list of MediaItems.
   
    Also, use the `verif = tweetClassificationbyUserMedia(item);` or the `verifs =                            tweetClassificationbyUserMedia(listMedia);` and 
   
   `verif = tweetClassificationTotalMedia(item);` or `verifs = tweetClassificationTotalMedia(listMedia);`
   
   for User and Total feature extraction respectively.
5. Use the function `printVerificationResults(verif);` to print the verification result.
   
We used three classification models for computing the verification results(s), one for each type of features. The models are trained on our dataset which consists of tweets containing reliable and not reliable images posted for the Hurricane Sandy natural disaster. You can find these models in the */resources/model* folder, named _j48.model_ and _j48updated.model_ for Item features, _j48user.model_ for User features and _j48total.model_ for Total features.

You can also use `ItemVerificationExample();` function in `TweetClassifier` class that implements the classification procedure we described above.


###Building models and classifying additional data###

The procedure of building models and classifying is implemented in the `ItemClassifier` class for the Item features case, in the `UserClassifier` class for the User features case and in the `TotalClassifier` class for the Total features case. Here are the steps of the process:

1. Follow the steps 1 and 2 from the previous section in order to obtain the appopriate MediaItem(s) in case they are stored in mongodb.
2. Define four distinct lists of MediaItems to store the fake and real tweets for the training and testing sets.
   `List<MediaItem> itemsFakeTrain = new ArrayList<MediaItem>();`
	 `List<MediaItem> itemsFakeTest  = new ArrayList<MediaItem>();`
	 `List<MediaItem> itemsRealTrain = new ArrayList<MediaItem>();`
	 `List<MediaItem> itemsRealTest  = new ArrayList<MediaItem>();`
3. Implement the classification process by calling 
   `ItemClassifier.doClassification(itemsFakeTrain, itemsFakeTest, itemsRealTrain, itemsRealTest);`
	 `UserClassifier.doClassification(itemsFakeTrain, itemsFakeTest, itemsRealTrain, itemsRealTest);` or
	 `TotalClassifier.doClassification(itemsFakeTrain, itemsFakeTest, itemsRealTrain, itemsRealTest);`	
    depending on the case.
    
    The `doClassification()` function is responsible for:
    * extracting the appropriate features from the tweets
    * annotating
    * creating the training and testing set
    * creating the classifier by calling the `createClassifier(isTrainingSet)` function that saves the new model. Here,      you can also define which classifier will be used as well as the path where your model file would be saved. You can     find some sample models created with additional data in the _/resources/model_ folder.
    *classifying tweets by calling the `classifyItems(isTestingSet);` function that uses the previously defined model       file to classify and printing the results.

You can also have a look at the `performClassificationExample()` function in `TweetClassifier` class that implements the above steps.

###Implementing cross-validation to additional data###

Here are the steps to cross-validate a set of data:

1. Follow the steps 1 and 2 from the first section in order to obtain the appopriate MediaItem(s) in case they are stored in mongodb.
2. Define two distinct lists of MediaItems to store the fake and real tweets according to the 2nd step of the previous section.
3. Implement the cross-validation method by calling

   `ItemClassifier.crossValidate(itemsFake, itemsReal);`

	 `UserClassifier.crossValidate(itemsFake, itemsReal);`
	 
	 `TotalClassifier.crossValidate(itemsFake, itemsReal);`
   depending on the case.

   The `crossValidate()` function is responsible for:
  * extracting the appropriate features from the tweets
  * annotating
  * implementing cross-validation to the data and printing the results. Here, you can define the type of the classifier    will be used. 

Part II
----------
We additionally describe the process of extracting features for a single tweet or even a list of tweets. This procedure constitutes part of the classification procedure described in Part I.

###Extracting Item(Content) features from a tweet###
The `ItemFeaturesExtractor` class implements the Item feature extraction. 

To extract Item features, you should use the `featureExtractionMedia(item)` function for a MediaItem or the `featureExtractionMedia(listMediaItems)` for a list of MediaItems. This function first checks the tweet text's language and proceeds to feature extraction only if the language is English, Spanish or German. We maintain only these tweets as we currently support sentiment words only in these languages.

###Extracting User features from the tweet's user###
The `UserFeatureExtractor` class implements the User feature extraction.
To extract User features, you should use the `userFeatureExtractionMedia(item)` function for a MediaItem or the `userFeatureExtractionMedia(listMediaItems)` for a list of MediaItems. This function finds the corresponding users to the tweets using the `getStreamUser(id)` given the user id and performs the feature extraction.

###Extracting Total features (Item & User features merged)###
The `TotalFeatureExtractor` class implements the Total feature extraction.
To extract Total features, you should use the `featureExtractionMedia(item)` function for a MediaItem or the  `featureExtractionMedia(listMediaItems)` for a list of MediaItems. This function combines the above two functions for Item and User feature extraction and produces the Total features.

Part III
----------------
This part uses the `AgreementBasedRetraining` class to classify the items with the agreement based retraining method.
The method uses two classifiers, one based on Item features and the second on User features. Then, after classifying
the testing set with both the classifiers, it finds the items that the classifiers' prediction agreed. Using the agreed items
as training set, it classifies the disagreed ones. It covers three separate cases:

1. Create training set with the agreed items for disagreed classification (`classifyDisagreedOnAgreed` function).
2. Create training set with the agreed items plus the initial training dataset (`classifyDisagreedOnUpdatedExistingModel` function)
3. Create training set with the agreed items plus the initial training dataset using a random set of equal number of fake and real samples. (`classifyDisagreedOnUpdatedExistingModelInstance` function)

Note: The method also integrates the bagging technique for classification. It uses a portion of the items of the training set to build a model and classify the samples and it repeats the process several times. The result of the predictions is the majority of the predictions of the classifiers.

Additional information
------------------------
###Project dependencies###
The computational-verification project is dependent on two SocialSensor projects:
* [Socialsensor-framework-common](https://github.com/socialsensor/socialsensor-framework-common) : This project contains main classes and interfaces to be used by other SocialSensor projects.
* [Socialsensor-framework-client](https://github.com/socialsensor/socialsensor-framework-client) : The wrappers for handling information in/from the supported MongoDB database.
* [Socialsensor-geo-util](https://github.com/socialsensor/geo-util) : A collection of utilities that help with the analysis and indexing of geographic data.


If you use this framework for your research, please include a citation to the following paper: Boididou, C., Papadopoulos, S., Zampoglou, M., Apostolidis, L., Papadopoulou, O., & Kompatsiaris, Y. (2018). [Detection and visualization of misleading content on Twitter](https://link.springer.com/article/10.1007/s13735-017-0143-x). International Journal of Multimedia Information Retrieval, 7(1), 71-86.

    @article{boididou2018detection,
      author = {Detection and visualization of misleading content on Twitter},
      title = {Boididou, Christina and Papadopoulos, Symeon and Zampoglou, Markos and Apostolidis, Lazaros and Papadopoulou, Olga and Kompatsiaris, Yiannis},
      journal = {International Journal of Multimedia Information Retrieval},
      volume={7},
      number={1},
      pages={71--86},
      year={2018},
      doi = "10.1007/s13735-017-0143-x",     
      publisher={Springer}
    }
    

###Contact information###
For further details, contact Symeon Papadopoulos (papadop@iti.gr) or Christina Boididou (boididou@iti.gr).
