import pandas as pd
years = []
districts = []
crimes = []
data = []
district = -1

# get DataFrame from Excel file
df = pd.read_excel(r'./data.xlsx')

# insert the years
for key in df:
    if type(key) == int:
        years.append(key)
        print("INSERT INTO year (year) VALUES (" + str(key) + ");")

# insert the remaining data
for row in df.values:
    r = row.tolist()

    # if the row has just 1 entry, it is a district name, insert it
    if str(r[1]) == 'nan':
        districts.append(r[0])
        print("INSERT INTO district (district) VALUES ('" + r[0] + "');")
        district+=1

    else:
        # insert the crime type if it's new
        if r[0] not in crimes:
            crimes.append(r[0])
            print("INSERT INTO crime (crime) VALUES ('" + r[0] + "');")

        # insert the actual data values for each year
        for i,year in enumerate(years):
            # print(districts[district], year, r[0], r[i+1])
            year_id = "(SELECT year_id FROM year WHERE year = " + str(year) + ")"
            district_id = "(SELECT district_id FROM district WHERE district = '" + districts[district] + "')"
            crime_id = "(SELECT crime_id FROM crime WHERE crime = '" + r[0] + "')"
            value = str(r[i+1])
            print("INSERT INTO data (year_id, district_id, crime_id, value) VALUES (" + year_id + ", " + district_id + ", " + crime_id + ", " + value + ");")