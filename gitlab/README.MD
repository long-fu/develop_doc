
sudo yum install -y curl policycoreutils-python openssh-server perl
# Enable OpenSSH server daemon if not enabled: sudo systemctl status sshd
sudo systemctl enable sshd
sudo systemctl start sshd
# Check if opening the firewall is needed with: sudo systemctl status firewalld
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo systemctl reload firewalld

sudo yum install postfix
sudo systemctl enable postfix
sudo systemctl start postfix

curl https://packages.gitlab.com/install/repositories/gitlab/gitlab-ce/script.rpm.sh | sudo bash


sudo EXTERNAL_URL="https://gitlab.example.com" yum install -y gitlab-ce
# List available versions: sudo yum --showduplicate list
# Specify version: sudo yum linstall gitlab-ce-16.1.4-ce.0.el7.x86_64
# Pin the version to limit auto-updates: yum versionlock gitlab-ce*

---

cat /etc/gitlab/initial_root_password


sudo EXTERNAL_URL="http://119.23.231.199:8010" yum install -y gitlab-ce

sudo yum linstall gitlab-ce-11.3.3-ce.0.el7.x86_64

vim /etc/gitlab/gitlab.rb

gitlab-ctl reconfigure

gitlab-ctl restart

