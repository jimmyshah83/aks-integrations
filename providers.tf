terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = ">= 3.7.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~>3.0"
    }
  }

  # Needs to be pre-configured in Azure either manually or via another Terraform script
  backend "azurerm" {
    resource_group_name  = "rg-tf-github-actions-state"
    storage_account_name = "tfgithubactions"
    container_name       = "tfstate"
    key                  = "terraform.tfstate"
    use_oidc             = true
  }
}

provider "azurerm" {
  features {}
  use_oidc = true
}