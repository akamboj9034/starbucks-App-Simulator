const express = require('express')
const path = require('path')
const PORT = process.env.PORT || 5000

var db = require('./db/config');
var UsersCollection = null;

db.connect(conn => {
    UsersCollection = conn;
})

var bodyParser = require('body-parser')


var engines = require('consolidate');

express()
    .use(express.static(path.join(__dirname, 'public')))
    .use(bodyParser.json())
    .use(bodyParser.urlencoded({
        extended: true
    }))
    .use(bodyParser.raw())
    .engine('html', engines.mustache)
    .set('views', path.join(__dirname, 'views'))
    .set('view engine', 'html')
    .get('/', (req, res) => {
        res.render('pages/home.html');
    })
    .get('/CreateNewCard.html', (req, res) => {
        res.render('pages/CreateNewCard.html');
    })
    .get('/CreateNewItem.html', (req, res) => {
        res.render('pages/CreateNewItem.html');
    })
    .post('/isUser', (req, res) => {
        isUser(req, res);
    })
    .post('/checkUserDiff', (req, res) => {
        checkUserDiff(req, res);
    })
    .post('/addUser', (req, res) => {
        addUser(req, res);
    })
    .post('/getUser', (req, res) => {
        getUser(req, res);
    })
    .post('/setPin', (req, res) => {
        setPin(req, res);
    })
    .post('/generateCard', (req, res) => {
        generateCard(req, res);
    })
    .post('/generateItem', (req, res) => {
        generateItem(req, res);
    })
    .post('/linkCard', (req, res) => {
        linkCard(req, res);
    })
    .post('/linkItem', (req, res) => {
        linkItem(req, res);
    })
    .post('/getTransactions', (req, res) => {
        getTransactions(req, res);
    })
    .listen(PORT, () => console.log(`Listening on ${PORT}`))

function setPin(req, res) {
    try {
        var json = req.body;

        console.log(json);
        console.log(req.body.user_email);
        console.log(req.body.old_passcode);
        console.log(req.body.new_passcode);
        console.log("data received");

        var reqKeys = ["user_email", "old_passcode", "new_passcode"];

        if (!checkIfJsonCotains(json, reqKeys)) {
            res.json({
                "success": false,
                "error": "insufficient parameters",
                "req": json
            });
            return;
        }

        UsersCollection.findOne({
            "user_email": json["user_email"],
            "passcode": json["old_passcode"]
        })
            .then(function (doc) {
                if (!doc) {
                    res.json({
                        "success": false,
                        "error": "invalid credentials",
                        "req": json
                    });
                    return;
                }

                var newTime = Date.now();

                UsersCollection.update({
                    "user_email": json["user_email"],
                    "passcode": json["old_passcode"]
                }, {
                        $set: {
                            "passcode": json["new_passcode"],
                            "last_changed": newTime
                        }
                    });
console.log("passcode updated");
data={

    "success": true,
    "newTime": newTime
}
                res.json({data});
            });




    } catch (err) {
        res.json({
            "success": false,
            "error": err.message
        });
    }
}

function checkUserDiff(req, res) {
    try {
        var json = req.body;

        console.log(json);
        console.log(req.body.user_email);
        console.log(req.body.passcode);
        console.log(req.body.last_changed);
        console.log("data received");

        var reqKeys = ["user_email", "passcode", "last_changed"];

        if (!checkIfJsonCotains(json, reqKeys)) {
            data={

                "success": false,
                "error": "insufficient parameters",
                "req": json
            }
            res.json({data});
            return;
        }

        UsersCollection.findOne({
            "user_email": json["user_email"],
            "passcode": json["passcode"]
        }, { "_id": false })
            .then(function (doc) {
                if (!doc) {
                    res.json({
                        "success": false,
                        "error": "No User Exists with this Email"
                    });
                    return;
                } else {

                    if (doc["last_changed"] == json["last_changed"]) {

                        res.json({
                            "success": true
                        });

                    } else {

                        delete doc["_id"];

                        res.json({
                            "success": true,
                            "data": doc
                        });

                    }

                    return;
                }

            });

    } catch (err) {
        send_data={

            "success": false,
            "error": err.message
        }
        res.json({send_data});
    }
}

function isUser(req, res) {
    try {
        var json = req.body;

        console.log(json);
        console.log(req.body.user_email);
        console.log(req.body.passcode);
        console.log("data received");

        var reqKeys = ["user_email"];

        if (!checkIfJsonCotains(json, reqKeys)) {
            res.json({
                "success": false,
                "error": "insufficient parameters",
                "req": json
            });
            return;
        }

        UsersCollection.findOne({
            "user_email": json["user_email"]
        })
            .then(function (doc) {
                if (!doc) {
                    res.json({
                        "success": false,
                        "error": "No User Exists with this Email"
                    });
                    return;
                } else {
                    res.json({
                        "success": true
                    });
                    return;
                }

            });

    } catch (err) {
        data={

            "success": false,
            "error": err.message
        }
        res.json({data});
    }
}


function getUser(req, res) {
    try {
        var json = req.body;

        console.log(json);
        console.log(req.body.user_email);
        console.log(req.body.passcode);
        console.log("data received");

        var reqKeys = ["user_email", "passcode"];

        if (!checkIfJsonCotains(json, reqKeys)) {
            data={

                "success": false,
                "error": "insufficient parameters",
                "req": json
            }
            res.json({data});
            return;
        }

        UsersCollection.findOne({
            "user_email": json["user_email"],
            "passcode": json["passcode"]
        }, { "_id": false })
            .then(function (doc) {
                if (!doc) {
                    res.json({
                        "success": false,
                        "error": "Invalid User"
                    });
                    return;
                }
                delete doc["_id"];
                res.json(doc);

            });

    } catch (err) {
        res.json({
            "success": false,
            "error": err.message
        });
    }
}

function addUser(req, res) {
    try {
        var json = req.body;

        console.log(json);
        console.log(req.body.user_email);
        console.log(req.body.passcode);
        console.log(req.body.last_changed);
        console.log("data received");

        

        var reqKeys = ["user_email", "passcode", "last_changed"];

        if (!checkIfJsonCotains(json, reqKeys)) {
            data={

                "success": false,
                "error": "insufficient parameters",
                "req": json
            }
            res.json({data});
            return;
        }

        var dbInput = {};

        for (var i = 0; i < reqKeys.length; i++) {
            try {
                dbInput[reqKeys[i]] = json[reqKeys[i]];
            } catch (err) {
                console.log(err.message)
            }
        }

        dbInput.cards = [];

        if (UsersCollection == null) {
            res.json({
                "success": false,
                "error": "MongoDb Error"
            });

            return;
        }
        UsersCollection.insertOne(dbInput, (err, result) => {
            if (err) {
                res.json({
                    "success": false,
                    "error": "User Already Exists"
                });
                return;
            }

            var newTime = Date.now();

            UsersCollection.update({
                "user_email": dbInput["user_email"]
            }, {
                    $set: {
                        "last_changed": newTime
                    }
                });

            res.json({
                "success": true,
                "newTime": newTime
            });
        })

    } catch (err) {
        res.json({
            "success": false,
            "error": err.message
        });
    }
}

function generateCard(req, res) {
    try {
        var json = req.body;
        if (!checkIfJsonCotains(json, ["dollars"])) {
            res.json({
                "success": false,
                "error": "insufficient parameters",
                "req": json
            });
            return;
        }

        var card_id = parseInt((Date.now() / 1000), 10) % 1000000000;
        var card_code = (Date.now() % 1000);

        card_id = pad(card_id, 9);
        card_code = pad(card_code, 3);

        var result = {
            "card_id": card_id,
            "card_code": card_code,
            "card_balance": parseFloat(json['dollars'])
        };

        res.json(result);

    } catch (err) {
        res.json({
            "success": false,
            "error": err.message
        });
    }
}

function generateItem(req, res) {
    try {
        var json = req.body;
        if (!checkIfJsonCotains(json, ["item_name", "item_price"])) {
            res.json({
                "success": false,
                "error": "insufficient parameters",
                "req": json
            });
            return;
        }

        res.json({
            "item_name": json["item_name"],
            "item_price": parseFloat(json["item_price"] + ""),
        });

    } catch (err) {
        data={

            "success": false,
            "error": err.message

        }
        res.json({data });
    }
}

function linkCard(req, res) {
    try {
        var json = req.body;

        console.log(json);
        console.log(req.body.card_id);
        console.log(req.body.card_code);
        console.log(req.body.card_balance);
        console.log(req.body.user_email);
        console.log(req.body.passcode);
        console.log("data received");

        if (!checkIfJsonCotains(json, ["card_id", "card_code", "card_balance", "user_email", "passcode"])) {
            res.json({
                "success": false,
                "error": "insufficient parameters",
                "req": json
            });
            return;
        }

        if (parseFloat(json['card_balance']) <= 0) {
            res.json({
                "success": false,
                "error": "Invalid Amount",
                "req": json
            });
            return;
        }

        UsersCollection.findOne({
            "user_email": json["user_email"],
            "passcode": json["passcode"]
        })
            .then(function (doc) {
                if (!doc) {
                    res.json({
                        "success": false,
                        "error": "no user available"
                    });
                    return;
                }

                console.log(doc.user_email);

                UsersCollection.findOne({
                    "cards.card_id": json["card_id"]
                })
                    .then(function (doc) {
                        if (doc) {
                            res.json({
                                "success": false,
                                "error": "card already in use"
                            });
                            return;
                        }

                        var newTime = Date.now();

                        UsersCollection.update({
                            "user_email": json["user_email"],
                            "passcode": json["passcode"]
                        }, {
                                $push: {
                                    "cards": {
                                        "id": newTime,
                                        "card_id": json["card_id"],
                                        "card_code": json["card_code"],
                                        "card_balance": parseFloat(json["card_balance"] + ""),
                                        "transactions": []
                                    }
                                }
                            });


                        UsersCollection.update({
                            "user_email": json["user_email"],
                            "passcode": json["passcode"]
                        }, {
                                $set: {
                                    "last_changed": newTime
                                }
                            });

                        res.json({
                            "success": true,
                            "newTime": newTime
                        });

                    });
            });



    } catch (err) {
        res.json({
            "success": false,
            "error": err.message
        });
    }
}

function linkItem(req, res) {
    try {
        var json = req.body;

        console.log(req.body.card_id);
        console.log(req.body.item_name);
        console.log(req.body.user_email);
        console.log(req.body.item_price);
        console.log(req.body.passcode);
        console.log("data received");


        if (!checkIfJsonCotains(json, ["card_id", "item_name", "item_price", "user_email", "passcode"])) {
            res.json({
                "success": false,
                "error": "insufficient parameters",
                "req": json
            });
            return;
        }
        data={
            "success": false,
            "error": "Invalid Amount",
            "req": json
        }
        if (parseFloat(json['item_price']) <= 0) {
            res.json({data});
            return;
        }

        UsersCollection.findOne({
            "user_email": json["user_email"],
            "cards.card_id": json["card_id"],
            "passcode": json["passcode"]
        })
            .then(function (doc) {
                if (!doc) {
                    res.json({
                        "success": false,
                        "error": "Invalid Details"
                    });
                    return;
                }

                console.log(doc.user_email);
                var cards = doc.cards;

                for (var i = 0; i < cards.length; i++) {
                    if (cards[i].card_id == json["card_id"]) {

                        if ((json["item_price"] > cards[i].card_balance)) {
                            data={
                                "success": true,
                            "newTime": newTime,
                            "oldtime":oldtime
                            }
                            res.json({data});
                            return;

                        }

                        var newTime = Date.now();

                        UsersCollection.update({
                            "user_email": json["user_email"],
                            "cards.card_id": json["card_id"],
                            "passcode": json["passcode"]
                        }, {
                                $push: {
                                    "cards.$.transactions": {
                                        "id": newTime,
                                        "item_name": json["item_name"],
                                        "item_price": parseFloat(json["item_price"] + "")
                                    }
                                },
                                $set: {
                                    "cards.$.card_balance": (cards[i].card_balance - parseFloat(json["item_price"] + ""))
                                }
                            });

                        UsersCollection.update({
                            "user_email": json["user_email"],
                            "passcode": json["passcode"]
                        }, {
                                $set: {
                                    "last_changed": newTime
                                }
                            });
                            data={
                                "success": true,
                            "newTime": newTime
                            }
                        res.json({data});

                        break;
                    }
                }

            });
    } catch (err) {
        res.json({
            "success": false,
            "error": err.message
        });
    }
}

function getTransactions(req, res) {
    try {
        var json = req.body;

        console.log(json);

        if (!checkIfJsonCotains(json, ["user_email", "passcode"])) {
            res.json({
                "success": false,
                "error": "insufficient parameters",
                "req": json
            });
            return;
        }

        UsersCollection.findOne({
            "user_email": json["user_email"],
            "passcode": json["passcode"]
        })
            .then(doc => {
                if (!doc) {
                    res.json({
                        "success": false,
                        "error": "Invalid Details"
                    });
                    return;
                }
                let transactions = [];
                doc.cards.forEach(v => {
                    v.transactions.forEach(v1 => {
                        transactions.push({ card: v.card_id, transaction: v1 });
                    });
                })
                data={
                    transction_data:transactions
                }
                res.json(data.transction_data);
            })
    } catch (err) {
        res.json({
            "success": false,
            "error": err.message
        })
    }
}

function pad(n, width, z) {
    z = z || '0';
    n = n + '';
    return n.length >= width ? n : new Array(width - n.length + 1).join(z) + n;
}

function checkIfJsonCotains(json, keys) {
    for (var i = 0; i < keys.length; i++) {
        if (json[keys[i]] == undefined) return false;
    }
    return true;
}