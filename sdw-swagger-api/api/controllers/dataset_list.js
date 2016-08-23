'use strict';

var util = require('util');
//var fs = require('fs')
var PropertiesReader = require('properties-reader');
// Mapping swagger operationId to functions in this file
module.exports = {
	getDatasets: getDatasets
};

function getDatasets(req, res) {
	var limit = req.swagger.params.limit.value;
	var properties = PropertiesReader("/Users/rsingh22/SDW/conf/datasets.properties");
	var paths = properties.get("paths");
	res.send(paths.split(","));
	//res.send(paths.split(","));
	//res.send("Dataset 1");
}