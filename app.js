var express = require('express')
var app = express()
var MongoClinet = require('mongodb').MongoClient
var url ="mongodb://localhost:27017/result"
var cursor

app.listen(3000,() =>{
    console.log("listening on port 3000...")
})

app.use(express.json())

MongoClinet.connect(url,(err,db) => {
    
    if (err){
        console.log("Error connecting to mongodb")
    }else{
        var myDb =db.db('result')
       
        var collection =myDb.collection('result')

        //Fetching Data from Result Database
        app.post('/DataResult',(req,res) =>{

           cursor=collection.find().toArray((err,result) => {

            if(result != null){

                res.status(200).send(result)
            }else{
                res.status(404).send()
            }

           });
        })

        //Getting the numbers of document in the database
        app.post('/Number',(req,res) =>{

            cursor=collection.countDocuments((err,result) => {
            
                if (result != null){
                    console.log(result)
                    res.status(200).send({result});
                }else{
                    res.status(404).send()
                }

            });

        })

    }

})

