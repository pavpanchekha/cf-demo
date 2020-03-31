CF=checker-framework-3.2.0
SRC=divbyzero
JAVAC=$(CF)/checker/bin/javac
JAVA_FILES=$(shell find $(SRC) -iname "*.java")

.PHONY: all build help test clean

all: build

build: $(JAVA_FILES) | $(JAVAC)
	$(JAVAC) -cp '$(CF)/checker/dist/checker.jar:$(CF)/checker/dist/javac.jar' $(JAVA_FILES)

test: build
	$(JAVAC) -sourcepath tests -classpath '$(shell pwd)' -processor divbyzero.DivByZeroChecker 'tests/Foo.java' || true

$(JAVAC): | $(CF).zip
	unzip '$(CF).zip'

$(CF).zip:
	curl -OLf 'https://checkerframework.org/$@'

clean:
	$(RM) -r '$(CF)' '$(CF).zip'
	find . -iname '*.class' -print -delete
	find . -iname '*.jar'   -print -delete
