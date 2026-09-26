# GitHub Setup Guide for UniApp-JDBC

## Step 1: Create Repository on GitHub

1. Go to https://github.com/new
2. Fill in the repository details:
   - **Repository name**: `UniApp-JDBC`
   - **Description**: `Academic project - University Application Management System using JDBC and Database Interoperability`
   - **Visibility**: Public (recommended for academic projects to be discoverable)
   - **Initialize with**: Leave unchecked (we'll push existing code)
   - **Add .gitignore**: Select "Java" (optional, already have one)
   - **Add a license**: Choose "MIT" or your preferred license

3. Click "Create repository"

## Step 2: Initialize Local Git Repository

```bash
cd /Users/mansapatidar/Downloads/HW-8
```

If not already a git repo:
```bash
git init
git add .
git commit -m "Initial commit: University Application Management System with JDBC DAL

- Implements DAO pattern with User, Applicant, Reviewer, and Rating models
- Secure database operations with parameterized queries
- Complete CRUD operations for all entities
- Connection management and transaction handling

Co-authored-by: Copilot App <223556219+Copilot@users.noreply.github.com>"
```

## Step 3: Add Remote and Push to GitHub

Replace `<your-username>` with your GitHub username if different from setup:

```bash
git remote add origin https://github.com/MansaPatidar/UniApp-JDBC.git
git branch -M main
git push -u origin main
```

## Step 4: Mark Repository as Academic on GitHub

Once the repo is on GitHub:

1. Go to your repository: https://github.com/MansaPatidar/UniApp-JDBC
2. Click **Settings** → **About** (right sidebar)
3. Add these topics (tags):
   - `academic`
   - `jdbc`
   - `java`
   - `database`
   - `dao-pattern`
   - `educational`

4. Check the box for **"This is a template repository"** (optional, helps others use it as a starting point)

## Step 5: Add Academic Project Badge (Optional)

Add this to your README.md to make it clear it's an academic project:

```markdown
![Academic Project](https://img.shields.io/badge/project-academic-blue)
![Java](https://img.shields.io/badge/language-java-red)
![JDBC](https://img.shields.io/badge/technology-JDBC-orange)
```

## Step 6: Verify Everything

After pushing, verify:
- ✅ README.md displays properly on GitHub
- ✅ Code files are visible
- ✅ Repository topics are set to "academic"
- ✅ .gitignore is working (no build/ or .idea/ folders)

## Troubleshooting

### "fatal: not a git repository"
```bash
cd /Users/mansapatidar/Downloads/HW-8
git status
```

### "fatal: remote origin already exists"
```bash
git remote remove origin
git remote add origin https://github.com/MansaPatidar/UniApp-JDBC.git
```

### Push is rejected
Check if branch exists and try:
```bash
git push -u origin main --force  # Only if you're sure!
```

---

## After Setup: Recommended Actions

1. **Add a LICENSE file** (already recommended MIT)
2. **Add CONTRIBUTING.md** if you want others to contribute
3. **Add Issues** for potential improvements
4. **Add Project board** for tracking work
5. **Enable Discussions** for Q&A

---

## Repository Structure (After Push)

```
UniApp-JDBC/
├── src/
│   └── main/java/HW8_Applications/
│       ├── model/
│       ├── dal/
│       ├── Driver.java
│       └── ConnectionManager.java
├── gradle/
├── build.gradle
├── gradlew
├── README.md
├── .gitignore
├── .github/  (optional - add workflows here)
└── LICENSE
```

---

**Next Steps**: After pushing to GitHub, you can:
- Add GitHub Actions for CI/CD
- Pin this repository to your profile
- Share the link with others for reference
