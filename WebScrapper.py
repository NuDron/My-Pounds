import requests
import urllib.request
import time
from datetime import datetime
from datetime import date
from bs4 import BeautifulSoup
from functools import wraps
from time import time


def timing(f):
    @wraps(f)
    def wrapper(*args, **kwargs):
        start = time()
        result = f(*args, **kwargs)
        end = time()
        print('Elapsed time: {}'.format(end-start))
        return result
    return wrapper

class webScrap:
    def __init__(self, url, name):
        self.url = url
        self.name = name

    def getResponseText(self):
        """
        Method parses the response to text from set url address.
        :return: an BeatifulSoup object
        """
        response = requests.get(self.url)
        soup = BeautifulSoup(response.text, "html.parser")
        return soup

    def setSelect(self, aString):
        """
        The method selects only elements with selected tag(aString) from parsed html text.
        :param aString: a tag to parse from text
        :return: a list
        """
        soup = self.getResponseText()
        soup = soup.select(aString)
        return soup

    def getDate(self):
        """
        The method utilises the library datetime to give "YYYY-MM-DD" output.
        :return:
        """
        myDate = date.today()
        return myDate

    def getTime(self):
        """
        The method utilises the datetime library to output time in "HH:MM:SS" format.
        :return:
        """
        myDate = datetime.today()
        currentTime = myDate.strftime("%H:%M:%S")
        return currentTime

    def produceDataEntry(self, firstEntry, secondEntry):
        try:
            result = []
            cTime = self.getTime()
            cDate = self.getDate()
            result.append(cDate)
            result.append(cTime)
            result.append(firstEntry)
            result.append(secondEntry)
            result.append(self.url)
            result.append(self.name)
            return result
        except Exception:
            pass
    def getCurrencyValues(self, anIntID):
        # TODO : DOCUMENTATION
        try:
            soup = self.setSelect('span')
            result = soup[anIntID].string
            result = result.replace(',','.')
            return result
        except Exception:
            pass

    # TODO : UNIT TESTS
    def makeFloat(self, aValue):
        """
        The method makes the passed aValue into float type. Used try/except statement for examples when
        the value passed is in Polish notation (with ',' instead of '.').
        :param aValue:
        :return: a Float value
        """
        try:
            result = float(aValue)
        except:
            result = aValue.replace(',', '.')
            result = float(result)
        return result

    def getCurrencyValuesJSON(self, intID):
        try:
            # Obtaining values from the website to parse.
            temp = str(self.getResponseText())
            # Separating the large scrapped text with ','.
            temp = temp.split(',')
            # Using the value passed into method to target sub-value and split it further.
            result = temp[intID].split(':')
            # Obtaining last value as 'usually' it is the value wanted.
            result = self.makeFloat(result[-1])
            return result
        except Exception:
            pass

    # Helping Method
    def scoutValues(self, aString=None):
        """
        The method is used for scouting all of the numerical values on the website.
        Example:
        scoutValues('span')
        Outputs:
        {15: 4.7614, 17: 4.7838, 22: 4.7569, 24: 4.7874} # Key is index of the num value(from all spans on website)
        :param aString: a HTML tag that will be searched for
        :return:
        """
        finalResult = {}
        if aString is not None:
            result = self.setSelect(aString)
            for x in range(len(result)):
                try:
                    val = result[x].string
                    try:
                        val = val.replace(",", ".")
                    except:
                        pass
                    val = float(val)
                except Exception:
                    pass  # To not "swallow" other errors
                if isinstance(val, float):
                    finalResult[x] = val
            if bool(finalResult):
                return finalResult
            else:
                for x in range(len(result)):
                    val = result[x].string
                    if isinstance(val, float):
                        finalResult[x] = val
                return finalResult
        else:
            result = str(self.getResponseText())
            result = result.split(',')
            for x in range(len(result)):
                finalResult[x] = result[x]
            return finalResult

    # Helping method
    def startTimedScrapping(self, anInt, tInterval, firstID, secondID):
        """
        BASIC TEST METHOD
        Method made for basic timed web scraping. Prints out anInt times at tInterval a scrapped data values(strings).
        :param anInt: number of for loop executions
        :param tInterval: time.sleep variable
        :return:
        """
        start = time.time()
        for x in range(anInt):
            soup = self.setSelect('span')
            end = time.time()
            print("Loop nr " + str(x + 1) + " ; " + str(end - start) + " sec passed" + "(interval " + str(
                tInterval) + "s).")
            print(soup[firstID].string)
            print(soup[secondID].string)
            time.sleep(tInterval)

def main():
    # Access the website
    url1 = 'https://cinkciarz.pl/wymiana-walut/kursy-walut-cinkciarz-pl/gbp/pln'
    # response = requests.get(url3)    # Response 200 means access was successful
    # Next we parse the html with BeautifulSoup so that we can work with a nicer, nested BeautifulSoup data structure.
    # soup = BeautifulSoup(response.text, "html.parser")
    # soup = soup.select('span')
    strona1 = webScrap(url1, "Cinkciarz.pl")
    url2 = "https://internetowykantor.pl/kurs-funta/"
    strona2 = webScrap(url2, "InternetowyKantor.pl")
    print(strona1.produceDataEntry(strona1.getCurrencyValues(22), strona1.getCurrencyValues(24)))
    url3 = "https://user.walutomat.pl/api/public/marketBrief/GBP_PLN"
    strona3 = webScrap(url3, "Walutomat.pl")
    #print(strona2.scoutValues('span'))
    print(strona2.produceDataEntry(strona2.getCurrencyValues(3), strona2.getCurrencyValues(6)))
    print(strona3.produceDataEntry(strona3.getCurrencyValuesJSON(2), strona3.getCurrencyValuesJSON(1)))

if __name__ == "__main__":
    main()


