terraform {
  required_version = ">= 1.5.0"

  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.110"
    }
  }
}

# ─────────────────────────────────────────────────────────────────────────────
# Provider
# ─────────────────────────────────────────────────────────────────────────────
provider "azurerm" {
  features {}
  # Credentials are read from environment variables:
  #   ARM_SUBSCRIPTION_ID, ARM_TENANT_ID, ARM_CLIENT_ID, ARM_CLIENT_SECRET
  # Or via: az login (Azure CLI)
}

# ─────────────────────────────────────────────────────────────────────────────
# Variables
# ─────────────────────────────────────────────────────────────────────────────
variable "resource_group_name" {
  description = "Name of the Azure Resource Group"
  type        = string
  default     = "healthcare-poc-rg"
}

variable "location" {
  description = "Azure region for all resources"
  type        = string
  default     = "East US"
}

variable "acr_name" {
  description = "Azure Container Registry name (globally unique, 5–50 alphanumeric chars)"
  type        = string
  default     = "healthcarepocacr"
}

variable "acr_sku" {
  description = "ACR pricing tier: Basic | Standard | Premium"
  type        = string
  default     = "Basic"

  validation {
    condition     = contains(["Basic", "Standard", "Premium"], var.acr_sku)
    error_message = "acr_sku must be one of: Basic, Standard, Premium."
  }
}

variable "tags" {
  description = "Tags applied to all resources"
  type        = map(string)
  default = {
    project     = "healthcare-poc"
    environment = "dev"
    managed_by  = "terraform"
  }
}

# ─────────────────────────────────────────────────────────────────────────────
# Resource Group
# ─────────────────────────────────────────────────────────────────────────────
resource "azurerm_resource_group" "main" {
  name     = var.resource_group_name
  location = var.location
  tags     = var.tags
}

# ─────────────────────────────────────────────────────────────────────────────
# Azure Container Registry
# ─────────────────────────────────────────────────────────────────────────────
resource "azurerm_container_registry" "acr" {
  name                = var.acr_name
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  sku                 = var.acr_sku

  # Enable admin credentials so docker login works with username/password
  # (disable in favour of managed identity for production workloads)
  admin_enabled = true

  tags = var.tags
}

# ─────────────────────────────────────────────────────────────────────────────
# Outputs — used by CI/CD pipelines to push images
# ─────────────────────────────────────────────────────────────────────────────
output "resource_group_name" {
  description = "Name of the provisioned resource group"
  value       = azurerm_resource_group.main.name
}

output "acr_login_server" {
  description = "ACR login server URL (e.g. healthcarepocacr.azurecr.io)"
  value       = azurerm_container_registry.acr.login_server
}

output "acr_admin_username" {
  description = "ACR admin username for docker login"
  value       = azurerm_container_registry.acr.admin_username
  sensitive   = true
}

output "acr_admin_password" {
  description = "ACR admin password for docker login"
  value       = azurerm_container_registry.acr.admin_password
  sensitive   = true
}
