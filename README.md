# GmailSender
This class is used to send Gmail in backgroung

Step - 1
Add below three library for mail
(1) activation.jar
(2) additionnal.jar
(3) mail.jar

Step - 2
Add this two class in your android project and add gmail username and
password.

Step - 3
Then just call senMail() methos ad bellow

GMainSender mail = GMainSender.getInstance();
mail.sendMail("Payment Request", "Response : " +  mResponseData, "hardik.p.bambhania@gmail.com");
