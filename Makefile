JAVA_MAIN_CLASS := com.markaldrich.glang.Main

SOURCEDIR := src/
CLASSDIR := bin./

JAVA_SOURCES := $(shell find $(SOURCEDIR) -name '*.java')
JAVA_SOURCES := $(subst $(SOURCEDIR), $(empty), $(JAVA_SOURCES))
JAVA_CLASSES := $(JAVA_SOURCES)
JAVA_CLASSES := $(JAVA_CLASSES:.java=.class)

all: pre-build main-build

clean:
	@echo -- Cleaning... --
	/bin/rm -r $(CLASSDIR)
	/bin/mkdir $(CLASSDIR)
	@echo -- Cleaning finished. --
	@printf "\n"

pre-build:
	@echo -- Executing pre-build... --
	cd $(SOURCEDIR) && find ./ -type d -exec /bin/mkdir -p -- ../bin{} \;
	@echo -- Done executing pre-build. -
	@printf "\n"
	
main-build: pre-build
	@printf "\n"
	@echo -- Compiling... --
	cd $(SOURCEDIR) && javac -d ../$(CLASSDIR) $(JAVA_SOURCES)
	@echo -- Done compiling. --
	@printf "\n"

run: main-build
	@echo -- Running... --
	cd bin./ && java -cp . $(JAVA_MAIN_CLASS)
	@echo -- Done running. --
	@printf "\n"