/* This script takes json data from https://raw.githubusercontent.com/daohoangson/dvhcvn/master/data/sorted.json  
 * And convert it to sql data at mContract
 *
*/

var cities = require('./dvhcvn.json')

class Dvhc {
	constructor(id, name, prefix, parent) {
		this.id = id;
		this.name = name;
		this.prefix = prefix;
		this.parent = parent;
	}
}

let allCities = [];
let allDistricts = [];
let allWards = [];

cities.forEach(city => {
	let cityId = city[0];
	let name = city[1];
	let prefix = city[2];
	let districts = city[4];

	allCities.push(new Dvhc(cityId, name, prefix, null));

	districts.forEach(district => {
		let districtId = district[0];
		let name = district[1];
		let prefix = district[2];
		let wards = district[4];

		allDistricts.push(new Dvhc(districtId, name, prefix, cityId))

		wards.forEach(ward => {
			let wardId = ward[0];
			let name = ward[1];
			let prefix = ward[2];
			allWards.push(new Dvhc(wardId, name, prefix, districtId))
		});
	})
})

// CITY
allCities.forEach(city => {
	console.log(`INSERT INTO dvhc_cities (id, name, prefix) VALUES(${city.id}, '${city.name}', '${city.prefix}');`);
})

// DISTRICT
allDistricts.forEach(district => {
	// Escape single quote in name
	if (district.name.includes("'")) {
		district.name = district.name.replace("'", "''");
	}
	console.log(`INSERT INTO dvhc_districts (id, name, prefix, fk_dvhc_city) VALUES(${district.id}, '${district.name}', '${district.prefix}', ${district.parent});`);
})

// WARD

allWards.forEach(ward => {
	// Escape single quote in name
	if (ward.name.includes("'")) {
		ward.name = ward.name.replace("'", "''");
	}
	console.log(`INSERT INTO dvhc_wards (id, name, prefix, fk_dvhc_district) VALUES(${ward.id}, '${ward.name}', '${ward.prefix}', ${ward.parent});`);
})
