file = open('input_yValues.txt','r')
counter = 0

for row in file:
    counter += 1
    r = row.replace("\n","")
    print('\t', r)
    if counter == 8:
        print("},")
        print("\n")
        print("{")
        counter = 0

file.close()