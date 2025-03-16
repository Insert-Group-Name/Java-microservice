# GitHub Secrets Setup Guide

For the deployment workflow to function correctly, you need to set up the following secrets in your GitHub repository:

## Required Secrets

1. **GCP_PROJECT_ID**
   - Your Google Cloud Platform project ID
   - Example: `my-project-12345`

2. **GCP_SA_KEY**
   - The JSON service account key for your Google Cloud Platform service account
   - This should be the entire JSON content of the service account key file
   - The service account needs to have the following roles:
     - App Engine Admin
     - Storage Admin
     - Service Account User

3. **ANTHROPIC_API_KEY**
   - Your Anthropic API key for Claude
   - Example: `sk-ant-api03-...`

## How to Set Up Secrets

1. Go to your GitHub repository
2. Click on "Settings"
3. In the left sidebar, click on "Secrets and variables" → "Actions"
4. Click on "New repository secret"
5. Add each of the secrets listed above

## Setting Up Google Cloud Service Account

1. Go to the [Google Cloud Console](https://console.cloud.google.com/)
2. Navigate to "IAM & Admin" → "Service Accounts"
3. Click "Create Service Account"
4. Give it a name like "github-actions"
5. Assign the required roles (App Engine Admin, Storage Admin, Service Account User)
6. Create a key for this service account (JSON format)
7. Download the key file
8. Copy the contents and paste them as the GCP_SA_KEY secret in GitHub

## Verifying Your Setup

Once you've added all the secrets, your GitHub Actions workflow should be able to deploy your application successfully to Google App Engine. 