# GitHub Webhook Setup for Automatic Jenkins Builds

This guide explains how to configure GitHub to automatically trigger Jenkins builds when code is pushed.

## Prerequisites
- Jenkins server running and accessible
- GitHub repository: https://github.com/LankeVenkatesh-Java-Developer/JenkinsPipeLineProject.git

## Step 1: Configure Jenkins for GitHub Webhooks

### 1.1 Install GitHub Plugin in Jenkins
1. Go to Jenkins Dashboard → Manage Jenkins → Plugins
2. Available plugins tab → Search for "GitHub Plugin"
3. Install and restart Jenkins

### 1.2 Configure Jenkins URL
1. Go to Manage Jenkins → Configure System
2. Scroll to "GitHub" section
3. Configure GitHub Server:
   - Name: GitHub
   - API URL: https://api.github.com
   - Credentials: Add GitHub personal access token (optional for public repos)

### 1.3 Enable Build Trigger in Jenkins Job
1. Open your Jenkins pipeline job
2. Click Configure
3. Under "Build Triggers", select:
   - ✅ GitHub hook trigger for GITScm polling
4. Save the job

## Step 2: Configure GitHub Webhook

### 2.1 Get Jenkins Webhook URL
The webhook URL format is:
```
http://<jenkins-server>/github-webhook/
```

For your local Jenkins:
```
http://localhost:8080/github-webhook/
```

If Jenkins is behind a firewall or on a different network, you'll need:
- Public IP or domain name
- Or use a tunneling service like ngrok

### 2.2 Add Webhook in GitHub
1. Go to your GitHub repository: https://github.com/LankeVenkatesh-Java-Developer/JenkinsPipeLineProject
2. Click Settings → Webhooks → Add webhook
3. Configure:
   - **Payload URL:** `http://<your-jenkins-url>/github-webhook/`
   - **Content type:** `application/json`
   - **Secret:** (optional) generate a secret token for security
   - **Which events would you like to trigger this webhook?**
     - ✅ Just the push event
     - Or select specific events as needed
4. Click "Add webhook"

### 2.3 Test the Webhook
1. In GitHub webhook settings, click "Recent Deliveries"
2. Click the latest delivery to see the response
3. Should show "200 OK" if successful

## Step 3: Test Automatic Build

### 3.1 Make a Code Change
```bash
# Make a small change to test
echo "# Test webhook" >> README.md
git add README.md
git commit -m "Test GitHub webhook trigger"
git push origin main
```

### 3.2 Verify Jenkins Build
1. Go to Jenkins Dashboard
2. Your job should automatically start building
3. Check the build logs to confirm it was triggered by GitHub webhook

## Troubleshooting

### Webhook Not Triggering
- Check Jenkins is accessible from GitHub (public URL required)
- Verify webhook URL is correct
- Check Jenkins logs for webhook errors
- Ensure "GitHub hook trigger" is enabled in job configuration

### Jenkins Not Responding to Webhook
- Check Jenkins is running: `docker ps`
- Check Jenkins logs: `docker logs <jenkins-container-id>`
- Verify GitHub plugin is installed
- Check firewall/network settings

### Using ngrok for Local Development
If Jenkins is running locally and not publicly accessible:

1. Install ngrok: https://ngrok.com/download
2. Run ngrok: `ngrok http 8080`
3. Use the ngrok URL in GitHub webhook:
   ```
   https://<random-id>.ngrok.io/github-webhook/
   ```
4. Note: ngrok free tier changes URL on restart

## Security Considerations

### Webhook Secret
1. Generate a secure secret token
2. Add it to GitHub webhook configuration
3. Configure Jenkins to validate the secret:
   - Manage Jenkins → Configure System → GitHub Server
   - Add secret in credentials

### Jenkins Security
- Enable CSRF protection in Jenkins
- Use authentication for Jenkins
- Restrict webhook access if needed

## Alternative: Poll SCM

If webhooks are not feasible, use Jenkins polling:

1. In Jenkins job configuration → Build Triggers
2. Select: ✅ Poll SCM
3. Schedule: `H/5 * * * *` (poll every 5 minutes)
4. Jenkins will check GitHub for changes every 5 minutes

## Summary

With webhooks configured:
- Every push to GitHub triggers Jenkins build automatically
- Build runs checkout, build, test, and archive stages
- JAR artifact is archived in Jenkins
- You can manually build Docker image from the archived JAR

## Next Steps

After webhook is working:
1. Consider adding Docker build stage back to Jenkins when Docker is available
2. Configure notifications (email, Slack) for build status
3. Set up deployment stages for production
