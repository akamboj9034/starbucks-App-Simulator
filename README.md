## Starbucks Group Project CMPE 202

### Team : Dragon 

1. Amit Kamboj: 013827489
2. Jessica Mathias : 013838097
3. Nihal Konda: 013828997
4. Pooja Kataria: 013818415
5. Prathamesh Patki: 013772395

### Project Architecture:

![Screen Shot 2019-05-12 at 1 19 05 AM](https://user-images.githubusercontent.com/47230108/57579684-63af0480-7454-11e9-8eeb-aaccdc2508df.png)

### Database:
Mongo DB is being used as database to store data as documents.

### Deployment:
APIs are deployed AWS in an Auto Scaled EC2 Cluster with Load Balancer.

### Individual Contributions :

#### 1. User Authentication API: Nihal Konda

Initially when a user tries to login, the isUser API is called which checks if the user is already present in the Mongo DB. If the user is a new user, the API returns success: false which shows that the user does not exist in the database. 

![isUser](https://user-images.githubusercontent.com/47230108/57579313-17ad9100-744f-11e9-891d-fd59f889c77f.png)

![getUser](https://user-images.githubusercontent.com/47230108/57579559-f6e73a80-7452-11e9-8c78-5268005e71bd.png)

Next the addUser API is called where the user enters his passcode and an account is created for the user which is shown in the POSTMAN as success:true

![addUser](https://user-images.githubusercontent.com/47230108/57579510-c8b52b00-7451-11e9-9e6f-2af7718b9ed0.png)

#### 2. Add Card API: Pooja Kataria

Once the user has logged in, he needs to enter a 9-digit card number and a 3-digit card code and a default balance. When he clicks on send in postman, generateCard API pushes the details into the Mongo DB stores the card details for the user. The linkCard API also stores multiple cards can be stored for a user.  The Add Card API is also validated against entering a card number lesser than 9 digits or entering a balance lesser than 0.

![generateCard](https://user-images.githubusercontent.com/47230108/57579504-b20ed400-7451-11e9-9ab1-4b7d78bf02c8.png)

![linkCard](https://user-images.githubusercontent.com/47230108/57579494-707e2900-7451-11e9-8db2-1a53b6453e27.png)

#### 3. Payment API: Jessica Mathias

Once the card is added, the user might want to order an item from starbucks and pay for that order. When the user enters the item name and item price, the linkItem API is called. The linkItem API validates the transaction against the card details and the card balance. Once the card details are validated, the necessary balance is deducted, and the API returns a success message. The new balance will be reflected in the user’s card details.

![linkItem](https://user-images.githubusercontent.com/47230108/57579497-8855ad00-7451-11e9-9182-78e8c37767f3.png)

#### 4. Managed Orders API: Amit Kamboj

The user might want to see a list of all the transactions he has made from his particular card. He can view these transactions using a getTransactions API which returns a list of transactions.

<img width="1440" alt="Screen Shot 2019-05-11 at 6 03 26 PM" src="https://user-images.githubusercontent.com/47230108/57588672-6b11f480-74cc-11e9-9eb1-0ede7b67fa3b.png">

#### 5. Reset Pin API: Prathamesh Patki

In order to reset the pin for the user, the setPin API is implemented. The user enters his old passcode and new passcode and clicks on submit. The new password for the user is reflected in Postman.

![SetPin](https://user-images.githubusercontent.com/47230108/57579532-1f226980-7452-11e9-8ca6-7af445ceaa26.png)

### Team Contributions (Extra Credit):

#### 1. Creating an AWS instance for our Starbucks App and implementing load balancer: Amit Kamboj, Jessica Mathias and Pooja Kataria

##### a. AWS Instance:

![Ec2 Instance](https://user-images.githubusercontent.com/47230108/57579571-27c76f80-7453-11e9-8ffd-ea21ec7d82ad.png)

##### b. AWS Load Balancer:

![Ec2 Load Balancer](https://user-images.githubusercontent.com/47230108/57579585-4fb6d300-7453-11e9-9bcf-fb034ab286b6.png)

##### c. AWS AutoScaling: 

![Ec2 AutoScaling](https://user-images.githubusercontent.com/47230108/57579814-509d3400-7456-11e9-96f2-3e80e58ecca9.png)

#### 2.Creating an android mobile application for Starbucks: Nihal Konda and Prathamesh Patki

![Login](https://user-images.githubusercontent.com/47230108/57579590-5e9d8580-7453-11e9-9e28-1802b8acdd11.png)           !       ![ChooseAccount](https://user-images.githubusercontent.com/47230108/57579594-63623980-7453-11e9-80c6-d862336e6229.png)                   ![setPasscode](https://user-images.githubusercontent.com/47230108/57579597-66f5c080-7453-11e9-8bc6-650da252db8f.png)

![reenterPasscode](https://user-images.githubusercontent.com/47230108/57579598-68bf8400-7453-11e9-92c8-0c9439b8b1a7.png)                    ![MyCardsFirstScreen](https://user-images.githubusercontent.com/47230108/57579601-6d843800-7453-11e9-80e0-4e7315b558e6.png)                   ![Settings](https://user-images.githubusercontent.com/47230108/57579602-6fe69200-7453-11e9-9eac-9f4271c1000a.png)

![AddCard](https://user-images.githubusercontent.com/47230108/57579605-77a63680-7453-11e9-9ad4-9e354aa8ebae.png)                    ![AddCardInfo](https://user-images.githubusercontent.com/47230108/57579607-7aa12700-7453-11e9-88fd-24762ff4bc71.png)                    ![AfterAddingCard](https://user-images.githubusercontent.com/47230108/57579609-7e34ae00-7453-11e9-8a87-22723f8ce468.png)

![MultipleCards](https://user-images.githubusercontent.com/47230108/57579610-7f65db00-7453-11e9-8534-f56c2df3f548.png)                    ![MyCardsPay](https://user-images.githubusercontent.com/47230108/57579612-82f96200-7453-11e9-80b2-acbf4bfd5c64.png)                   ![MyCardsPaySuccess](https://user-images.githubusercontent.com/47230108/57579614-842a8f00-7453-11e9-8699-9b2098d693c1.png)

![ViewTxn](https://user-images.githubusercontent.com/47230108/57579615-87257f80-7453-11e9-843c-a5e07f2f1965.png)                    ![ResetOldPassword](https://user-images.githubusercontent.com/47230108/57579617-8bea3380-7453-11e9-8cdb-367ce1138d7f.png)                   ![ResetOldPassword2](https://user-images.githubusercontent.com/47230108/57579619-8db3f700-7453-11e9-84dd-8806f4551678.png)

![ResetOldPassword3](https://user-images.githubusercontent.com/47230108/57579620-91e01480-7453-11e9-9bdd-f9934ad156f6.png)
