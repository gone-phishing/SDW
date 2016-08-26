'use strict';

var util = require('util');

module.exports = {
	validateUser: validateUser
};

function validateUser(req, res) {
	var token = req.swagger.params.credentials.value;
	var user = req.body.username;
	var pass = req.body.password;
	console.log(user+" : "+pass);
	if (user.match("a@a.com") && pass.match("a")) 
	{
		res.setHeader('Content-Type', 'text/html');
		res.send("Welcome to Smart Data Web!");
	} 
	else 
	{
		res.setHeader('Content-Type', 'text/html');
		res.statusCode = 404;
		res.send("Authentication failed!");
	}
}