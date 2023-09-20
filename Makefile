# Makefile for planner docker build

app_name :="cloudlab"
ecr_repo :="344678476167.dkr.ecr.ap-southeast-1.amazonaws.com/cloudlab"
ver := $(shell cat ./version)
port ?= 8098
debug_port := $(shell expr $(port) + 100)
data_dir ?= /data

.DEFAULT_GOAL := help

.PHONY: help build

pull: ## update code from git
	@git pull --rebase

build: ## build by gradlew
	@./gradlew clean build -x test --refresh-dependencies

build-fast: ## build by gradlew
	@./gradlew clean build -x test

build-docker: ## build a docker image
	@docker build --force-rm -t $(app_name):$(ver) .

tag-image: ## deploy a docker image
	@docker tag $(app_name):$(ver) $(ecr_repo):$(ver)

push: ## push to docker repository
	@docker push $(ecr_repo):$(ver)

deploy-docker: build-fast build-docker tag-image push## deploy app by compose
	@echo $(ver)

up: ## [Docker] boot by docker
	@docker-compose build && docker-compose up -d

deploy: pull build up ## deploy app by compose
	@docker-compose logs -f

deploy-fast: pull build-fast up ## deploy app by compose
	@docker-compose logs -f

restart: ## start/restart app not rebuild
	@docker-compose restart

stop: ## stop app
	@docker-compose stop

ps: ## view docker-compose ps
	@docker-compose ps

log: ## view dokcer-compose logs
	@docker-compose logs -f

clean-images: ## [Docker] delete old docker image
	@docker images | grep ${app_name} | awk '{print $3}' | tr '\n' ' ' | xargs docker rmi -f

version: ## show current version of app
	@echo $(ver)

update: ## update app version
	@mvn release:update-versions -DdevelopmentVersion=$(ver)

help: ## show help
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)
