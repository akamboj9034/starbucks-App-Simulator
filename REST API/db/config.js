const MongoClient = require('mongodb').MongoClient;

const uri = "mongodb+srv://starbucksapp:Mrox@5497*com@cluster0-ajhzw.mongodb.net/test?retryWrites=true";

const client = new MongoClient(uri, {
	useNewUrlParser: true
});

exports.connect = (callback) => {
	client.connect(err => {
		let UsersCollection = client.db("StarbucksApp").collection("Users");
		callback(UsersCollection);
	});
}